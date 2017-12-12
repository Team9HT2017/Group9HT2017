%%%-------------------------------------------------------------------
%%% @author Laiz Figueroa
%%% @author Anthony Path
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is the client connection
%%% @end
%%% Created : 26. Oct 2017 13:08
%%%-------------------------------------------------------------------
-module(ehaus_client).

-export([start/1]).
-export([start_link/1, init/2]).
-export([system_continue/3, system_terminate/4,
  system_get_state/1, system_replace_state/2, distribute/2]).

-record(s, {socket = none :: none | gen_tcp:socket()}).

start(ListenSocket) ->
  ehaus_client_sup:start_acceptor(ListenSocket).

start_link(ListenSocket) ->
  proc_lib:start_link(?MODULE, init, [self(), ListenSocket]).

init(Parent, ListenSocket) ->
  ok = io:format("~p Listening.~n", [self()]),
  Debug = sys:debug_options([]),
  ok = proc_lib:init_ack(Parent, {ok, self()}),
  listen(Parent, Debug, ListenSocket).

listen(Parent, Debug, ListenSocket) ->
  case gen_tcp:accept(ListenSocket) of
    {ok, Socket} ->
      % socketManager:addSocket(Socket),
      {ok, _} = start(ListenSocket),
      {ok, Peer} = inet:peername(Socket),
      ok = io:format("~p Connection accepted from: ~p~n", [self(), Peer]),
      State = #s{socket = Socket},
      loop(Parent, Debug, State);
    {error, closed} ->
      % socketManager:deleteSocket(Socket),
      ok = io:format("~p Retiring: Listen socket closed.~n", [self()]),
      exit(normal)
  end.

loop(Parent, Debug, State = #s{socket = Socket}) ->
  receive

    {tcp, Socket, <<"GET\n">>} -> % request to get map (ans username at the same time)
      M = mapStorage:get_map(),
      {ok, Peer} = inet:peername(Socket),
      {IP, _} = Peer,
      Username = userNameHandler:assignUserName(IP),
      io:format("~p Map: ~n", [M]),
      ok = gen_tcp:send(Socket, [M, <<"*">>, Username, "\n"]), % this is how we concatenate binary string, just put like a list
      ok = gen_tcp:shutdown(Socket, read_write),
      exit(normal);
    {tcp, Socket, InitialMessage} -> % all message requests
      Identifier = string:split(InitialMessage, "!?!", all), % find out what client wants
      [Message, Ident] = Identifier,
      case Ident of <<"TEACHER\n">> -> % teacher wants to put user names and map
        Content = string:split(Message, "!^!", all),
        [Map, Usernames] = Content,
        transferMessage:resetCounter(),
        mapStorage:send(Map), % send map to storage
        userNameHandler:putUserNames(Usernames), % send user name list to storage
        {ok, Peer} = inet:peername(Socket),
        {IP, _} = Peer,
        Username = userNameHandler:assignTeacher(IP),
        io:format("Teacher: ~tp~n", [Socket]),
        P = gen_tcp:send(Socket, [list_to_binary(Username), "\n"]),
        io:format("Sending: ~tp~n", [P]),
        loop(Parent, Debug, State);
        <<"STUDENT\n">> -> % student wants to send message
          ok = io:format("~p received: ~tp~n", [self(), Message]), % "test!^!SEND!?!STUDENT\n"
          Mess = string:split(Message, "!^!", all),
          [ActualMessage, Type] = Mess,
          case Type of <<"SEND">> ->  % sender sends message
            R1 = string:split(ActualMessage, "to ", all),
            %Con=transferMessage:confirm_message(ActualMessage),
            gen_tcp:send(Socket, "Success\n"),
            io:format("~p Splitted.~n", [R1]),
            [_, Part] = R1,
            [To, _] = string:split(Part, ","),
            io:format("~p Recipient: ~n", [To]),
            RecipTry = userNameHandler:get_Username(To),
            case RecipTry of [] -> ok; % distribute(Users,Distributive)
              [{IP, _}] ->
                MessID = transferMessage:store_message(ActualMessage),
                [{IP, _}] = RecipTry,
                io:format("Message: ~p~n", [ActualMessage]),
                {ok, SocketSend} = gen_tcp:connect(IP, 6789, []),
                % SocketSend
                io:format("Message ID: ~p~n", [MessID]),
                ok = gen_tcp:send(SocketSend, ([integer_to_binary(MessID), ",", ActualMessage, "\n"]));
              L -> K = [IP || {IP, _} <- L], % more users than nodes
                Y = lists:usort(K),
                MessID = transferMessage:store_message(ActualMessage),
                distribute(Y, ([integer_to_binary(MessID), ",", ActualMessage]))

            end;
            <<"CONFIRM">> ->    % recipient sends confirmation (ID of message as actual message)
              ForSearch = binary_to_integer(ActualMessage),
              Distributive = transferMessage:find_message(ForSearch),
              io:format("Message:~p~n", [Distributive]),
              Users = userNameHandler:get_list(),
              {ok, Peer1} = inet:peername(Socket),
              {NoSend, _} = Peer1,
              io:format("NoSend: ~p~n", [NoSend]),
              io:format("Users: ~p~n", [Users]),
              Check = [IP1 || {IP1, Us} <- Users],%IP1=/=NoSend],
              NoDups = lists:usort(Check),
              io:format("Users2: ~p~n", [Check]),
              distribute(NoDups, Distributive)

          end;
          <<"SEARCH\n">> ->
            User=userNameHandler:add_name(Message),
            io:format("~p the message sent: ~n",User),
            %Lis=userNameHandler:add_name(Message),
            %gen_tcp:send(Socket,[Lis, <<"&">>, "Remoove", "\n"]),
            loop(Parent, Debug, State)
      end;
    {tcp_closed, Socket} ->
      ok = io:format("~p Socket closed, retiring.~n", [self()]),
      exit(normal);
    {system, From, Request} ->
      sys:handle_system_msg(Request, From, Parent, ?MODULE, Debug, State);
    Unexpected ->
      ok = io:format("~p Unexpected message: ~tp", [self(), Unexpected]),
      loop(Parent, Debug, State)
  end.

distribute([], Dist) -> ok;
distribute([L | T], Dist) -> IP = L,  % sending message to multiple users
  {ok, SocketSend} = gen_tcp:connect(IP, 6789, []),
  io:format("Distributing: ~p~n", [IP]),
  io:format("Socket: ~p~n", [SocketSend]),
  K = gen_tcp:send(SocketSend, [Dist, "\n"]),
  io:format("Result: ~p~n", [K]),
  distribute(T, Dist).



system_continue(Parent, Debug, State) ->
  loop(Parent, Debug, State).

system_terminate(Reason, _Parent, _Debug, _State) ->
  exit(Reason).

system_get_state(Misc) -> {ok, Misc}.

system_replace_state(StateFun, Misc) ->
  {ok, StateFun(Misc), Misc}.

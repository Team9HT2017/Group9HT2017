%%%-------------------------------------------------------------------
%%% @author Fahd Debbiche
%%% @author Anthony Path
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%%
%%% @end
%%% Created : 20. Nov 2017 23:41
%%%-------------------------------------------------------------------
-module(transferMessage).
-author("fahddebbiche").

%% API
-export([start/0, store_message/1, confirm_message/1, find_message/1, resetCounter/0]).

% a module responsible  for exchanging messages between clients

start() ->
  case whereis(transferMessage) of
    undefined ->
      Pid = spawn(fun() ->
        io:format("Message handler operational~n"),
        message_loop([], 0, 0)
                  end),
      register(transferMessage, Pid),
      {ok, Pid};
    Pid -> {ok, Pid}
  end.


resetCounter() ->
  transferMessage ! {self(), reset},
  receive {_, ok} -> ok end.

store_message(Message) ->  % function to send message to server
  transferMessage ! {self(), send, Message},
  receive {_, M} -> M end.

confirm_message(Message) ->        % function to send confirmation of reception to the server
  transferMessage ! {self(), confirm, Message},
  receive {_, ok} -> ok;
    {_, nope} -> nope
  end.

find_message(ID) ->
  transferMessage ! {self(), find, ID},
  receive {_, M} -> M;
    nope -> not_found end.


message_loop(Messages, ID, Check) -> %message handling loop
  receive
    {Pid, reset} -> % reset message loop, when new map is uploaded
      Pid ! {self(), ok},
      message_loop([], 0, 0);
    {Pid, confirm, Message} -> % message confirmation case
      [_, Num] = string:split(Message, "?"),
      {K, _} = string:to_integer(Num),
      io:format("Integer...~p~n", [K]),
      if (K =:= Check) -> io:format("Ok...~p~n", [K]),
        Pid ! {self(), ok},
        message_loop(Messages, ID, Check + 1);
        (K =/= Check) -> io:format("Stupid...~p~n", [K]),
          Pid ! {self(), nope},
          message_loop(Messages, ID, Check)
      end;
    {Pid, send, Message} ->
      Add = {Message, ID},
      IDNew = ID + 1,
      Pid ! {self, ID},
      message_loop([Add | Messages], IDNew, Check);
    {Pid, find, Id} ->
      io:format("Searching...~p~n", [Id]),
      L = [Message || {Message, Ident} <- Messages, Ident =:= Id],
      case L of [] -> Pid ! nope,
        message_loop(Messages, ID, Check);
        [X] -> % [Mess]=L,
          Pid ! {self(), X},
          message_loop(lists:delete(X, Messages), ID, Check)
      end

  end.
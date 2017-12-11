%%%-------------------------------------------------------------------
%%% @author Anthony Path
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%%
%%% @end
%%% Created : 19. Нояб. 2017 18:14
%%%-------------------------------------------------------------------
-module(userNameHandler).

-export([start/0, putUserNames/1, get_list/0, assignUserName/1, get_Username/1, assignTeacher/1, findElement/2]).

%Functions related to storing and sending user names on the server
start() ->
  case whereis(userNameHandler) of
    undefined ->
      P = spawn(fun() ->
        io:format("Username handler operational ~n"),
        loopUserNames(<<"Nope\n">>, <<"No_one">>, <<"Nothing">>)
                end),
      register(userNameHandler, P),
      {ok, P};
    P -> {ok, P}
  end.

putUserNames(UserNameList) -> % function to send usernames, used by teacher
  userNameHandler ! {self(), put, UserNameList},
  receive {_, ok} -> ok end.

get_list() ->  %  function to get user names list from the loop
  userNameHandler ! {self(), get},
  receive {_, List} -> List end.

assignTeacher(IP) -> % function to assign teacher to the session
  userNameHandler ! {self(), teacher, IP},
  receive {_, List, ok} -> L = [lists:flatten([",", US]) || {_, US} <- List],
    [L | <<"!US!">>]
  end.

assignUserName(IP) -> % function to assign user name to new user
  userNameHandler ! {self(), assign, IP},
  receive {_, Username, ok} ->
    lists:flatten([Username, <<"\n">>]) end. % so that Java can read WTH is going on (username)

get_Username(Name) ->  % function to get a specific username from a list
  userNameHandler ! {self(), search, Name},
  receive {found, UsernameID} -> UsernameID;
    not_found -> not_found end.


findElement(0, [E | T]) -> E;
findElement(ElemIndex, [E | T]) ->
  findElement(ElemIndex - 1, T).

loopUserNames(List, Teacher, Usernames) -> % User names handling loop, which is connected to all client processes
  receive
    {Pid, put, New} ->
      Pid ! {self(), ok},
      NameList = string:split(New, ",", all), % splitting user name list to string
      io:format("~p List ~n", [NameList]),
      loopUserNames(NameList, Teacher, NameList);
    {Pid, teacher, Socket} ->  % assign all usernames to teacher for now
      Newlist = [{Socket, Us} || Us <- List],
      io:format("~p List ~n", [Newlist]),
      Pid ! {self(), Newlist, ok},
      loopUserNames(Newlist, Socket, Usernames);
    {Pid, assign, Socket} ->    %assigning user name to client. Taking (and deleting) user name from the list, making a pair of it and IP, and putting this pair to the list
      Check = [{IPC, US} || {IPC, US} <- List, IPC =:= Teacher],
      R = lists:flatlength(Check),
      io:format("Start list: ~p~n", [List]),
      io:format("Teacher check: ~p~n", [Check]),
      io:format("Teacher check length: ~p~n", [R]),
      if R =:= 1 -> Rand = rand:uniform(lists:flatlength(Usernames)),
        Username = findElement(Rand - 1, Usernames),
        ToPut = {Socket, Username},
        NewList2 = [ToPut | List], %
        Pid ! {self, Username, ok},
        io:format("Single list: ~p~n", [NewList2]),
        loopUserNames(NewList2, Teacher, Usernames);
        R > 1 ->
          {Teach, Username} = lists:last(List),
          ToPut = {Socket, Username},
          NewList = [ToPut | lists:delete({Teach, Username}, List)], %
          Tosend = [lists:flatten([",", US]) || {IPC, US} <- NewList, IPC =:= Teacher],
          {ok, SocketSend} = gen_tcp:connect(Teacher, 6789, []),
          ok = gen_tcp:send(SocketSend, [list_to_binary([Tosend | <<"!US!">>]), "\n"]),
          Pid ! {self, Username, ok},
          io:format("~p New List ~n", [NewList]),
          loopUserNames(NewList, Teacher, Usernames)
      end;
    {Pid, search, Name} ->
      io:format("~p Name: ~n", [Name]),
      L = [{Socket, Name} || {Socket, Username} <- List, Name =:= Username],
      Pid ! {found, L},
      loopUserNames(List, Teacher, Usernames);
    {Pid, get} ->
      Pid ! {self(), List},
      loopUserNames(List, Teacher, Usernames)
  end.
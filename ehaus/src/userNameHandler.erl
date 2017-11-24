%%%-------------------------------------------------------------------
%%% @author Lone ranger
%%% @copyright (C) 2017, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 19. Нояб. 2017 18:14
%%%-------------------------------------------------------------------
-module(userNameHandler).
-author("Lone ranger").


-export([start/0,putUserNames/1,get_list/0,assignUserName/1,get_Username/1]).


%Functions related to storing and sending user names on the server
start() ->
  case whereis(userNameHandler) of
    undefined ->
      P = spawn(fun () ->
        io:format("Username handler operational ~n"),
        loopUserNames(<<"Nope\n">>)
                end),
      register(userNameHandler, P),
      {ok, P};
    P -> {ok, P}
  end.

putUserNames(UserNameList)-> % function to send usernames, used by teacher
  userNameHandler ! {self(),put,UserNameList},
  receive {_,ok} -> ok end.

get_list()->  %  function to get user names list from the loop
  userNameHandler ! {self(),get},
  receive {_,List} ->List end.

assignUserName(Socket)-> % function to assign user name to new user
  userNameHandler ! {self(),assign,Socket},
  receive {_,Username,ok} -> lists:flatten(string:replace(Username,<<"+">>,<<"\n">>)) end. % so that Java can read WTH is going on (username)

get_Username(Name) ->  % function to get a specific username from a list
  userNameHandler ! {self(),search,Name},
  receive {found, UsernameID}-> UsernameID;
  not_found -> not_found end.

loopUserNames(List)-> % User names handling loop, which is connected to all client processes
  receive
    {Pid,put,New} ->
      Pid ! {self(),ok},
      NameList = string:split(New,",",all), % splitting user name list to string
      io:format("~p List ~n",[NameList]),
      loopUserNames(NameList);
    {Pid,assign,Socket} -> Username=lists:last(List), %assigning user name to client. Taking (and deleting) user name from the list, making a pair of it and IP, and putting this pair to the list
      ToPut={Socket,Username},
      NewList=[ToPut|lists:delete(Username,List)], %
      Pid ! {self,Username,ok},
      io:format("~p New List ~n",[NewList]),
      loopUserNames(NewList);
    {Pid,search,Name} ->
      L=[ {Socket,Name} || {Socket,Username} <- List,  Name=:=Username],
      case L of [] -> Pid ! not_found;
      true ->Pid ! {found, L}
      end,
      loopUserNames(List);
    {Pid,get} ->
      Pid ! {self(),List},
      loopUserNames(List)
  end.


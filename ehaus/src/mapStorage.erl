%%%-------------------------------------------------------------------
%%% @author Anthony Path
%%% @copyright (C) 2017, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 08. Нояб. 2017 17:43
%%%-------------------------------------------------------------------
-module(mapStorage).


-export([start/0, send/1, get_map/0]).


%Functions related to storing map on the server
start() ->
  case whereis(mapStorage) of
    undefined ->
      P = spawn(fun() ->
        io:format("Map storage operational ~n"),
        loopMap(<<"Nope\n">>)
                end),
      register(mapStorage, P),
      {ok, P};
    P -> {ok, P}
  end.

send(Map) -> % function to send map to map loop
  mapStorage ! {self(), put, Map},
  receive {_, ok} -> ok end.

get_map() ->  %  function to get map from map loop
  mapStorage ! {self(), get},
  receive {_, M} -> M end.

loopMap(Map) -> % Map loop, which is connected to all client processes
  receive
    {Pid, put, New} ->
      Pid ! {self(), ok},
      loopMap(New);
    {Pid, get} ->
      Pid ! {self(), Map},
      loopMap(Map)
  end.
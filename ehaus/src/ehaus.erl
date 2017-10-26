%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This class runs the Erlang application calling the client manager
%%% and making it listen to a specific port given by the application.
%%% @end
%%% Created : 26. Oct 2017 15:10
%%%-------------------------------------------------------------------
-module(ehaus).

-behaviour(application).

-export([listen/1, ignore/0]).
-export([start/0, start/1]).
-export([start/2, stop/1]).

listen(PortNum) ->
    ehaus_client_manager:listen(PortNum).

ignore() ->
    ehaus_client_manager:ignore().

start() ->
    application:ensure_started(sasl),
    application:start(?MODULE),
    io:format("Starting es...").


start(Port) ->
    ok = start(),
    ok = ehaus_client_manager:listen(Port),
    io:format("Startup complete, listening on ~w~n", [Port]).

start(normal, _Args) ->
    ehaus_sup:start_link().

stop(_State) ->
    ok.
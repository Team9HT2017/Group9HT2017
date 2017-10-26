%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This class is Part of the Supervisor Tree being a supervisor for
%%% the client.
%%% @end
%%% Created : 26. Oct 2017 15:42
%%%-------------------------------------------------------------------
-module(ehaus_client_sup).

-behaviour(supervisor).

-export([start_acceptor/1]).
-export([start_link/0]).
-export([init/1]).

start_acceptor(ListenSocket) ->
  supervisor:start_child(?MODULE, [ListenSocket]).

start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, none).

init(none) ->
  RestartStrategy = {simple_one_for_one, 1, 60},
  Client    = {ehaus_client,
    {ehaus_client, start_link, []},
    temporary,
    brutal_kill,
    worker,
    [ehaus_client]},
  {ok, {RestartStrategy, [Client]}}.
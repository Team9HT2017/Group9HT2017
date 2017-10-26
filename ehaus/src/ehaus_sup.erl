%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is part of the Supervisor Tree being a supervisor for
%%% the client supervisor.
%%% @end
%%% Created : 26. Oct 2017 10:39
%%%-------------------------------------------------------------------
-module(ehaus_sup).

-behaviour(supervisor).

-export([start_link/0]).
-export([init/1]).

start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, []).

init([]) ->
  RestartStrategy = {one_for_one, 1, 60},
  Clients   = {ehaus_clients,
    {ehaus_clients, start_link, []},
    permanent,
    5000,
    supervisor,
    [ehaus_clients]},
  Children  = [Clients],
  {ok, {RestartStrategy, Children}}.
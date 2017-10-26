%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is part of the Supervisor Tree being a supervisor for
%%% the client supervisor.
%%% @end
%%% Created : 26. Oct 2017 12:58
%%%-------------------------------------------------------------------
-module(ehaus_clients).

-behavior(supervisor).

-export([start_link/0]).
-export([init/1]).

start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, none).

init(none) ->
  RestartStrategy = {rest_for_one, 1, 60},
  ClientSup = {ehaus_client_sup,
    {ehaus_client_sup, start_link, []},
    permanent,
    5000,
    supervisor,
    [ehaus_client_sup]},
  ClientMan = {ehaus_client_manager,
    {ehaus_client_manager, start_link, []},
    permanent,
    5000,
    worker,
    [ehaus_client_manager]},
  Children  = [ClientSup, ClientMan],
  {ok, {RestartStrategy, Children}}.
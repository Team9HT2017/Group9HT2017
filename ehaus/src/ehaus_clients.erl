%%%-------------------------------------------------------------------
%%% @author Laiz Figueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is the second level of the Supervisor Tree being a supervisor
%%% for the client supervisor.
%%% @end
%%% Created : 26. Oct 2017 12:58
%%%-------------------------------------------------------------------
-module(ehaus_clients).

-behavior(supervisor).

-export([start_link/0]).
-export([init/1]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever ehaus_sup is started.
%%
%% @end
%%--------------------------------------------------------------------
start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, none).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called to start ehaus_client_sup supervisor and
%% ehaus_client_manager worker under this supervisor with the definitions
%% explained below.
%%
%% @end
%%--------------------------------------------------------------------
init(none) ->
  RestartStrategy = {rest_for_one, 1, 60}, %% If you need to restart a
  %% process that is dependent on each other in a chain. X works alone,
  %% but Y depend on it and Z depend on both. It restart the dependent
  %% processes not the other way around.

  ClientSup = {ehaus_client_sup, %% ChildId - Used for debugging
    {ehaus_client_sup, start_link, []}, %% Start function -> {Module,
    %% Function, Argument}
    permanent,%% Restart = permanent -> It is a process that always
    %% should be restarted, no matter what. Used by vital, long-living
    %% processes.
    5000, %% Shutdown
    supervisor,%% Type = supervisor
    [ehaus_client_sup]}, %% Modules -> The name of the callback module
  %% used by the child behaviour

  ClientMan = {ehaus_client_manager, %% ChildId - Used for debugging
    {ehaus_client_manager, start_link, []}, %% Start function -> {Module,
    %% Function, Argument}
    permanent, %% Restart = permanent -> It is a process that always
    %% should be restarted, no matter what. Used by vital, long-living
    %% processes.
    5000, %% Shutdown
    worker,%% Type = worker
    [ehaus_client_manager]}, %% Modules -> The name of the callback
  %% module used by the child behaviour

  Children = [ClientSup, ClientMan],

  {ok, {RestartStrategy, Children}}.
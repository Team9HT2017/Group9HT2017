%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is top-level of the Supervisor Tree being a supervisor for
%%% the client supervisor.
%%% @end
%%% Created : 26. Oct 2017 10:39
%%%-------------------------------------------------------------------
-module(ehaus_sup).

-behaviour(supervisor).

-export([start_link/0]).
-export([init/1]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application is started on ehaus
%% module.
%%
%% @end
%%--------------------------------------------------------------------
start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, []).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called to start ehaus_clients supervisor under
%% this supervisor with the definitions explained below.
%%
%% @end
%%--------------------------------------------------------------------
init([]) ->
  RestartStrategy = {one_for_one, 1, 60}, %% If one fail just that one
  %% restart. Use it to supervise an independent process or when you can
  %% lose the actual state without impacting others. It holds a list of
  %% all the children it has started, ordered by their starting order.

  Clients   = {ehaus_clients, %% ChildId -> Used for debugging
    {ehaus_clients, start_link, []}, %% Start function -> {Module,
    %% Function, Argument}
    permanent, %% Restart = permanent -> It is a process that always
    %% should be restarted, no matter what. Used by vital, long-living
    %% processes.
    5000, %% Shutdown
    supervisor, %% Type = supervisor
    [ehaus_clients]}, %% Modules -> The name of the callback module used
    %% by the child behaviour

  Children  = [Clients],

  {ok, {RestartStrategy, Children}}.
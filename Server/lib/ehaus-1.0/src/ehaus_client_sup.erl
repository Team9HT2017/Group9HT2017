%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This third level of the Supervisor Tree being a supervisor for
%%% the client.
%%% @end
%%% Created : 26. Oct 2017 15:42
%%%-------------------------------------------------------------------
-module(ehaus_client_sup).

-behaviour(supervisor).

-export([start_acceptor/1]).
-export([start_link/0]).
-export([init/1]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called to start ehaus_clients is started with an
%% specific socket
%%
%% @end
%%--------------------------------------------------------------------
start_acceptor(ListenSocket) ->
  supervisor:start_child(?MODULE, [ListenSocket]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called to start ehaus_clients is started
%%
%% @end
%%--------------------------------------------------------------------
start_link() ->
  supervisor:start_link({local, ?MODULE}, ?MODULE, none).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called to start ehaus_client worker under
%% this supervisor with the definitions explained below.
%%
%% @end
%%--------------------------------------------------------------------
init(none) ->
  RestartStrategy = {simple_one_for_one, 1, 60}, %% It takes only one
  %% type of child, in this case Client. It is used when you want to
  %% dynamically add children. It holds a single definition for all
  %% its children and uses a dictionary to hold its data - faster for
  %% huge amount of children.
  %% Integers = MaxRestart, MaxTime

  Client    = {ehaus_client, %% ChildId - Used for debugging
    {ehaus_client, start_link, []}, %% Start function -> {Module,
    %% Function, Argument}
    transient, %% Restart = transient -> It is a process that are
    %% meant to run until they terminate normally, and then they
    %% will not be restarted.
    brutal_kill,
    worker, %% Shutdown
    [ehaus_client]},%% Modules -> The name of the callback module used
    %% by the child behaviour

  {ok, {RestartStrategy, [Client]}}.
%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc This class runs the supervisor for this application,
%%%      which can restart the system once for each 10 seconds.
%%% @end
%%% Created : 11. Oct 2017 15:42
%%%-------------------------------------------------------------------

-module(erlServer_sup).

-behaviour(supervisor).

%% API
-export([start_link/0]).

%% Supervisor callbacks
-export([init/1, start_socket/0, terminate_socket/0, empty_listeners/0]).

-define(SERVER, ?MODULE).

%%--------------------------------------------------------------------
%% @doc
%% Starts the supervisor
%%
%% @end
%%--------------------------------------------------------------------
start_link() ->
    supervisor:start_link({local, ?SERVER}, ?MODULE, []).

%%%===================================================================
%%% Supervisor callbacks
%%%===================================================================

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Whenever a supervisor is started using supervisor:start_link/[2,3],
%% this function is called by the new process to find out about
%% restart strategy, maximum restart frequency and child
%% specifications.
%%
%% @end
%%--------------------------------------------------------------------
init([]) -> % restart strategy 'one_for_one': if one goes down only that one is restarted
  io:format("~p (~p) starting...~n"),
  {ok,
        {{one_for_one, 5, 30}, % The flag - 5 restart within 30 seconds
            [{erlServer_server, {erlServer_server, init, []}, permanent, 1000, worker, [erlServer_server]}]}}.
            % CHILD: name of the module we call: Server; the name of the function: start_server
            % The argument we will use = []; Restart permanent => always restart;
            % Shutdown = milliseconds (times to do correctly) or infinity; Type = worker/supervisor;
            % Module = erl_Server

%%%===================================================================
%%% Internal functions
%%%===================================================================

start_socket() ->
  supervisor:start_child(?MODULE, []).

terminate_socket() ->
  supervisor:delete_child(?MODULE, []).

empty_listeners() ->
  [start_socket() || _ <- lists:seq(1,20)],
  ok.
%% Start with 20 listeners so that many multiple connections can
%% be started at once, without serialization. In best circumstances,
%% a process would keep the count active at all times to insure nothing
%% bad happens over time when processes get killed too much.
%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc This class is the server itself with all the functions to start
%%%      a process and close if asked.
%%% @end
%%% Created : 11. Oct 2017 16:27
%%%-------------------------------------------------------------------

-module(erlServer_server).

%% API
-export([init/0, start_server/0]).

%% Defining the port used.
-define(PORT, 8080).

%%%===================================================================
%%% API
%%%===================================================================

%%--------------------------------------------------------------------
%% @doc
%% Initializes the server
%%
%% @end
%%--------------------------------------------------------------------
init() ->
	start_server().

%%%===================================================================
%%% Server callbacks
%%%===================================================================

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Starts the server
%%
%% @end
%%--------------------------------------------------------------------
start_server() ->
 io:format("Server started.~n"),
 Pid = spawn_link(fun() ->
   {ok, ServerSocket} = gen_tcp:listen(?PORT, [binary, {packet, 0}, % packet, raw
   {reuseaddr, true}, {active, true}]),
  io:format("~p", [ServerSocket]),
    server_loop(ServerSocket) end),
  {ok, Pid}.

server_loop(ServerSocket) ->
 {ok, Socket} = gen_tcp:accept(ServerSocket),
  Pid1 = spawn(fun() -> erlServer_client:client() end),
  inet:setopts(Socket, [{packet, 0}, binary, %% To change between active and passive modes when listening to messages.
    {nodelay, true}, {active, true}]),
  gen_tcp:controlling_process(Socket, Pid1), %% Let this PID guy take over my socket,
  %% He will be able to read and receive messages - Change ownership
  server_loop(ServerSocket).

%%%===================================================================
%%% Internal functions
%%%===================================================================



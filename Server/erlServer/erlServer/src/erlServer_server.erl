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
-export([init/0, client/0, send/0]).

%% Defining the port used.
-define(Port, 8080).

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
 {ok, ServerSocket} = gen_tcp:listen(?Port, [binary, {packet, 0}, % packet, raw
   {reuseaddr, true}, {active, true}]),
  io:format("~p", [ServerSocket]),
    server_loop(ServerSocket).

server_loop(ServerSocket) ->
 {ok, Socket} = gen_tcp:accept(ServerSocket),
  Pid = spawn(fun() -> handle_client(Socket) end),
  inet:setopts(Socket, [{packet, 0}, binary, %% To change between active and passive modes when listening to messages.
    {nodelay, true}, {active, true}]),
  gen_tcp:controlling_process(Socket, Pid), %% Let this PID guy take over my socket,
  %% He will be able to read and receive messages - Change ownership
  server_loop(ServerSocket).

handle_client(Socket) ->
  receive
%%   {tcp_closed, Socket, <<"quit">>} ->
%%     gen_tcp:close(socket); %% To close the connection when quit is typed. MAYBE NOT NEEDED
    {tcp, Socket, Request} ->
      io:format("received: ~s~n", [Request]),
      handle_client(Socket)
  end.

%%%===================================================================
%%% Internal functions
%%%===================================================================

client() ->
  io:format("Starting client. Enter \'quit\' to exit.~n"),
  connect(),
  display_prompt(),
  client_loop().

connect() ->
  gen_tcp:connect(). %% Need to be worked

send(Packet) ->
  {ok, Socket, Packet} = gen_tcp:send(Socket, Packet),
  {error, Reason} = io:format("Error faced: ~s~n", [Reason]).

recv(Packet) ->
  {recv, ok, Packet} = gen_tcp:recv(Socket, Packet),
  {recv, error, Reason} = io:format("Error faced: ~s~n", [Reason]).

% The prompt is handled by a separate process.
% Every time a message is entered (and io:get_line()
% returns) the separate process will send a message
% to the main process (and terminate).

display_prompt() ->
  Client = self(),
  spawn(fun () ->
    Packet = io:get_line("> "),
    Client ! {entered, Packet}
        end),
    Client ! {requested, Packet},
  ok.

client_loop() ->
  receive
    {send, ok, Packet}        ->
      io:format("~s", [Packet]),
      client_loop();
    {recv, ok, Packet}        ->
      io:format("~s", [Packet]),
      client_loop();
    {entered, "quit\n"} ->
      leave();
    {entered, Packet}        ->
      % When a packet is entered we receive it,
      recv(Packet),
      display_prompt(),
      client_loop();
    {requested, Packet}        ->
      % When a packet is requested we send it,
      send(Packet),
      display_prompt(),
      client_loop()
  end.

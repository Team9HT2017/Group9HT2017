%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc
%%% This class is the server itself with all the functions to start
%%% a process and close if asked.
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
    io:format("Baba~p", [ServerSocket]),
    server_loop(ServerSocket) end),
  {ok, Pid}.

server_loop(ServerSocket) ->
  io:format("Oba~p", [ServerSocket]),
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  Pid1 = spawn(fun() -> client() end),
  inet:setopts(Socket, [{packet, 0}, binary, %% To change between active and passive modes when listening to messages.
    {nodelay, true}, {active, true}]),
  gen_tcp:controlling_process(Socket, Pid1), %% Let this PID guy take over my socket,
  %% He will be able to read and receive messages - Change ownership
  server_loop(ServerSocket).

%%%===================================================================
%%% Internal functions
%%%===================================================================

%%--------------------------------------------------------------------
%% @doc
%% Starts a new client
%%
%% @end
%%--------------------------------------------------------------------
client() ->
  io:format("Starting client. Enter \'quit\' to exit.~n"),
  Client = self(),
  {ok, Sock} = gen_tcp:connect("localhost", ?PORT, [{active, false}, {packet, 2}]),
  display_prompt(Client),
  client_loop(Client, Sock).

%%%===================================================================
%%% Client callbacks
%%%===================================================================

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Send and receive packages
%%
%% @end
%%-------------------------------------------------------------------
send(Sock, Packet) ->
  {ok, Sock, Packet} = gen_tcp:send(Sock, Packet),
  io:format("Sent ~n").

recv(Packet) ->
  {recv, ok, Packet} = gen_tcp:recv(Packet),
  io:format("Received ~n").

% The prompt is handled by a separate process.
% Every time a message is entered (and io:get_line()
% returns) the separate process will send a message
% to the main process (and terminate).

display_prompt(Client) ->
  spawn(fun () ->
    Packet = io:get_line("> "),
    Client ! {entered, Packet}
        end),
  Client ! {requested},
  ok.

%%--------------------------------------------------------------------
%% @private
%% @doc
%% Client loop
%%
%% @end
%%-------------------------------------------------------------------

client_loop(Client, Sock) ->
  receive
    {entered, "quit\n"} ->
      gen_tcp:close(Sock);
    {entered, Packet}        ->
      % When a packet is entered we receive it,
      recv(Packet),
      display_prompt(Client),
      client_loop(Client, Sock);
    {requested, Packet}        ->
      % When a packet is requested we send it,
      send(Sock, Packet),
      display_prompt(Client),
      client_loop(Client, Sock);
    {error, timeout} ->
      io:format("Send timeout, closing!~n", []),
      Client ! {self(),{error_sending, timeout}},
      gen_tcp:close(Sock);
    {error, OtherSendError} ->
      io:format("Some other error on socket (~p), closing", [OtherSendError]),
      Client ! {self(),{error_sending, OtherSendError}},
      gen_tcp:close(Sock)
  end.

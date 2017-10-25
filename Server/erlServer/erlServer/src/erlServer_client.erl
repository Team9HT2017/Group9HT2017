%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc
%%%
%%% @end
%%% Created : 25. Oct 2017 09:04
%%%-------------------------------------------------------------------

-module(erlServer_client).

%% API
-export([client/0]).

%% Defining the port used.
-define(PORT, 8080).

%%%===================================================================
%%% API
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
send(Packet) ->
  {ok, Sock, Packet} = gen_tcp:send(Sock, Packet),
  {error, Reason} = io:format("Error faced: ~s~n", [Reason]).

recv(Packet) ->
  {recv, ok, Packet} = gen_tcp:recv(Packet),
  {recv, error, Reason} = io:format("Error faced: ~s~n", [Reason]).

% The prompt is handled by a separate process.
% Every time a message is entered (and io:get_line()
% returns) the separate process will send a message
% to the main process (and terminate).

display_prompt(Client) ->
  spawn(fun () ->
    Packet = io:get_line("> "),
    Client ! {entered, Packet}
        end),
  Client ! {requested, Packet},
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
    {entered, Packet}        ->
      % When a packet is entered we receive it,
      recv(Packet),
      display_prompt(Client),
      client_loop(Client, Sock);
    {requested, Packet}        ->
      % When a packet is requested we send it,
      send(Packet),
      display_prompt(Client),
      client_loop(Client, Sock);
    {error, timeout} ->
      io:format("Send timeout, closing!~n", []),
      Client ! {self(),{error_sending, timeout}},
      gen_tcp:close(Sock);
    {error, OtherSendError} ->
      io:format("Some other error on socket (~p), closing", [OtherSendError]),
      Client ! {self(),{error_sending, OtherSendError}},
      gen_tcp:close(Sock);
    {entered, "quit\n"} ->
      gen_tcp:close(Sock)
  end.
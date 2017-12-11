%%%-------------------------------------------------------------------
%%% @author Laiz Figueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This is a gen_server with all the functions to start a process and
%%% close if asked.
%%% @end
%%% Created : 26. Oct 2017 15:10
%%%-------------------------------------------------------------------
-module(ehaus_client_manager).

-behavior(gen_server).

-export([listen/1, ignore/0]).
-export([start_link/0]).
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
  code_change/3, terminate/2]).

-record(s, {port_num = none :: none | inet:port_number(),
  listener = none :: none | gen_tcp:socket()}).

listen(PortNum) ->
  gen_server:call(?MODULE, {listen, PortNum}),
  mapStorage:start(),
  transferMessage:start(),
  userNameHandler:start().

ignore() ->
  gen_server:cast(?MODULE, ignore).

start_link() ->
  gen_server:start_link({local, ?MODULE}, ?MODULE, none, []).

init(none) ->
  ok = io:format("Starting.~n"),
  State = #s{},
  {ok, State}.

handle_call({listen, PortNum}, _, State) ->
  {Response, NewState} = do_listen(PortNum, State),
  {reply, Response, NewState};
handle_call(Unexpected, From, State) ->
  ok = io:format("~p Unexpected call from ~tp: ~tp~n", [self(), From, Unexpected]),
  {noreply, State}.

handle_cast(ignore, State) ->
  NewState = do_ignore(State),
  {noreply, NewState};
handle_cast(Unexpected, State) ->
  ok = io:format("~p Unexpected cast: ~tp~n", [self(), Unexpected]),
  {noreply, State}.

handle_info(Unexpected, State) ->
  ok = io:format("~p Unexpected info: ~tp~n", [self(), Unexpected]),
  {noreply, State}.

code_change(_, State, _) ->
  {ok, State}.

terminate(_, _) ->
  ok.

do_listen(PortNum, State = #s{port_num = none}) ->
  SocketOptions =
    [{active, once},
      {mode, binary},
      {packet, 2},
      {keepalive, true},
      {reuseaddr, true}],
  {ok, Listener} = gen_tcp:listen(PortNum, SocketOptions),
  {ok, _} = ehaus_client:start(Listener),
  {ok, State#s{port_num = PortNum, listener = Listener}};
do_listen(_, State = #s{port_num = PortNum}) ->
  ok = io:format("~p Already listening on ~p~n", [self(), PortNum]),
  {{error, {listening, PortNum}}, State}.

do_ignore(State = #s{listener = none}) ->
  State;
do_ignore(State = #s{listener = Listener}) ->
  ok = gen_tcp:close(Listener),
  State#s{listener = none}.
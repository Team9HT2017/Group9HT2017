%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc
%%%
%%% @end
%%% Created : 11. Oct 2017 08:30
%%%-------------------------------------------------------------------

-module(server).
-behavior(gen_server).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%																			  	 	API			 				  	  													 %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

-export([start_link/0, stop/0, ask/1]).
-export([init/1, handle_call/3, handle_cast/2, handle_info/2, code_change/3, terminate/2]). %% WHY TWO DIFFERENT export?


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%																					INTERFACE			 																		 %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

start_link() ->
	gen_server:start_link({global, ?MODULE}, ?MODULE, [], []).

stop() ->
	gen_server:call({global, ?MODULE}, stop).

ask(_Message) ->
  gen_server:call({global, ?MODULE}, message).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%																					CALLBACKS			 																		 %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

init([]) -> % I need to think what it will do
	.

handle_call(message, _From, State) ->
  {ok, Answers} = application:get_env(haus, answers),
  Answer = element(rand:uniform(tuple_size(Answers)), Answers),
  {reply, Answer, State};
handle_call(stop, _From, State) ->
  {stop, normal, ok, State};
handle_call(_Call, _From, State) ->
  {noreply, State}.

handle_cast(_Cast, State) ->
  {noreply, State}.

handle_info(_Msg, State) ->
  {noreply, State}.

code_change(_OldVersion, State, _Extra) ->
  {ok, State}.

terminate(_Reason, _State) ->
  ok.


%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, <HAUS PROJECT>
%%% @doc This class runs the Erlang application calling the supervisor.
%%%
%%% @end
%%% Created : 11. Oct 2017 15:10
%%%-------------------------------------------------------------------

-module(erlServer_app).
-behaviour(application).

%% Application callbacks
-export([start/2, stop/1]).

%%====================================================================
%% API
%%====================================================================

start(_StartType, _StartArgs) ->
    erlServer_sup:start_link().

stop(_State) ->
    ok.


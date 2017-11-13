%%%-------------------------------------------------------------------
%%% @author LFigueroa
%%% @copyright (C) 2017, Haus Project
%%% @doc
%%% This class runs the Erlang application calling the client manager
%%% and making it listen to a specific port given by the application.
%%% @end
%%% Created : 26. Oct 2017 15:10
%%%-------------------------------------------------------------------
-module(ehaus).

-behaviour(application).

-export([listen/1, ignore/0]).
-export([start/0, start/1]).
-export([start/2, stop/1]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application is started using
%% application:start/[Port] as a auxiliary function or by the normal
%%
%% @end
%%--------------------------------------------------------------------
listen(PortNum) ->
    ehaus_client_manager:listen(PortNum).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called by the application in order to ignore a
%% client, calling the action on the server ehaus_client_manager.
%%
%% @end
%%--------------------------------------------------------------------
ignore() ->
    ehaus_client_manager:ignore().

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application is started using
%% application:start/[Port] as a auxiliary function or by the normal
%% application:start/0. It should start the processes of the
%% application. According to the OTP design principles it starts the
%% top supervisor of the tree and also make the manager listen to the
%% port
%%
%% @end
%%--------------------------------------------------------------------
start() ->
    application:ensure_started(sasl),
    application:start(?MODULE),
    io:format("Starting server...").

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application is started using
%% application:start/[Port], and it should start the processes of the
%% application. According to the OTP design principles it starts the
%% top supervisor of the tree and also make the manager listen to the
%% port
%%
%% @end
%%--------------------------------------------------------------------
start(Port) ->
    ok = start(),
    ok = ehaus_client_manager:listen(Port),
    %% ok = function -> always check that the returned value was the expected
    %% one, and not invisible errors happening in the background.
    io:format("Startup complete, listening on ~w~n", [Port]).

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application is started using
%% application:start/[normal,2], and should start the processes of the
%% application. According to the OTP design principles it starts the
%% top supervisor of the tree.
%%
%% @end
%%--------------------------------------------------------------------
start(normal, _Args) ->
    ehaus_sup:start_link().

%%--------------------------------------------------------------------
%% @private
%% @doc
%% This function is called whenever an application has stopped. It
%% is intended to be the opposite of Module:start/2 and should do
%% any necessary cleaning up. The return value is ignored.
%%
%% @end
%%--------------------------------------------------------------------
stop(_State) ->
    ok.

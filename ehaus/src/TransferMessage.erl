%%%-------------------------------------------------------------------
%%% @author fahddebbiche 
%%% @author Anthony Path
%%% @copyright (C) 2017, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 20. Nov 2017 23:41
%%%-------------------------------------------------------------------
-module(transferMessage).
-author("fahddebbiche").

%% API
-export([start/0,store_message/1,confirm_message/1,find_message/1]).

% a module responsible  for exchanging messages between clients

start() ->
  case whereis(transferMessage) of
    undefined ->
      Pid = spawn(fun () ->
        io:format("Message handler operational~n"),
        message_loop([],0,0)
                  end),
      register(transferMessage, Pid),
      {ok, Pid};
    Pid -> {ok, Pid}
  end.

store_message(Message)->  % function to send message to server
  transferMessage ! {self(),send,Message},
  receive {_,M} -> M end.

confirm_message(Message)->        % function to send confirmation of reception to the server
  transferMessage ! {self(),confirm,Message},
  receive {_,ok} ->ok;
    {_,nope}->nope
  end.

find_message (ID) ->
  transferMessage ! {self(),find,ID},
  receive {_,M}->M ;
    nope -> not_found end.
%search_message (Message) ->
% transferMessage ! {self(),find,Message},
%receive {_,X}->X ;
% nope -> not_found end.


message_loop(Messages,ID,Check)-> %message handling loop
  receive
    {Pid,confirm,Message}->
      [_,Num] = string:split(Message,"?"),
      %[C,_]=string:split(Num,"]"),
      {K,_}=string:to_integer(Num),
      io:format("Integer...~p~n",[K]),
      if (K=:=Check) ->  io:format("Ok...~p~n",[K]),
        Pid ! {self(),ok},
        message_loop(Messages,ID,Check+1);
        (K=/=Check) ->  io:format("Stupid...~p~n",[K]),
          Pid ! {self(),nope},
          message_loop(Messages,ID,Check)
        end;
    {Pid,send,Message} ->
     Add={Message,ID},
      IDNew=ID+1,
      Pid ! {self,ID},
      message_loop([Add|Messages],IDNew,Check);
    {Pid,find,Id} ->
      io:format("Searching...~p~n",[Id]),
      L=[Message||{Message,Ident}<-Messages,Ident=:=Id],
      case L of [] -> Pid ! nope;
        [X] -> % [Mess]=L,
          Pid ! {self(),X} end,
      %message_loop(Messages,ID);

      %  {Pid,find,Message} ->
      %   L=[Mess||{Mess,Ident}<-Messages,Mess=:=Message],
      %  case L of [] -> Pid ! nope;
      %   [X] -> % [Mess]=L,
      %    Pid ! {self(),X},
      message_loop(Messages,ID,Check)
  end.

%%client() ->
%%  client_loop().
%%
%%client_loop() ->       %client meesage loop handles the upcoming message to the client
%%  receive
%%    {send, Pid, message} ->
%%      Pid ! {received,ok},
%%      confirm_message(),
%%      client_loop();
%%    {rest,Pid,message} ->
%%      Pid ! {get,ok},
%%      client_loop()
%%
%%  end.


%%%-------------------------------------------------------------------
%%% @author fahddebbiche
%%% @copyright (C) 2017, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 20. Nov 2017 23:41
%%%-------------------------------------------------------------------
-module(transferMessage).
-author("fahddebbiche").

%% API
-export([start/0,store_message/1,confirm_message/0,find_message/1]).

% a module responsible  for exchanging messages between clients

start() ->
  case whereis(transferMesaage) of
    undefined ->
      Pid = spawn(fun () ->
        io:format("Message handler operational~n"),
        message_loop([],0)
                  end),
      register(transferMesaage, Pid),
      {ok, Pid};
    Pid -> {ok, Pid}
  end.

store_message(Message)->  % function to send message to server
  transferMesaage ! {self(),send,Message},
  receive {_,M} -> M end.

confirm_message()->        % function to send confirmation of reception to the server
  transferMesaage ! {self(),received},
  receive {_,ok} ->ok end.

find_message (ID) ->
  transferMessage ! {self(),find,ID},
  receive {_,M}->M ;
    nope -> not_found end.


message_loop(Messages,ID)-> %message handling loop
  receive
    {Pid,send,Message} ->
     Add={Message,ID},
      ID++
      Pid ! {self,Add},
      message_loop([Add|Messages],ID);
    {Pid,find,Id} ->
      L=[Message||{Message,Ident}<-Messages,Ident=:=Id],
      case L of [] -> Pid ! nope;
        true ->  {Mess,_}=L,
          Pid ! {self(),Mess} end,
      message_loop(Messages,ID)
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


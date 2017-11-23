%%%-------------------------------------------------------------------
%%% @author fahddebbiche
%%% @copyright (C) 2017, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 20. Nov 2017 23:41
%%%-------------------------------------------------------------------
-module('TransferMessage').
-author("fahddebbiche").

%% API
-export([start/0,send_message/2,confirm_message/0,client/0]).

% a module responsible  for exchanging messages between clients

start() ->
  case whereis(transferMesaage) of
    undefined ->
      Pid = spawn(fun () ->
        io:format("The server process is running!~n"),
        message_loop([])
                  end),
      register(transferMesaage, Pid),
      {ok, Pid};
    Pid -> {ok, Pid}
  end.

send_message(message,P)->  % function to send message to server
  transferMesaage ! {self(),send,message,P},
  receive {_,ok} -> ok end.
confirm_message()->        % function to send confirmation of reception to the server
  transferMesaage ! {self(),received},
  receive {_,ok} ->ok end.

message_loop(message)-> %message handling loop
  receive
    {Pid,send,meesage,P} ->
      P ! {send,self(),message},
      Pid ! {messagesent,ok},
      message_loop(message);
    {received,ok} ->
      List=userNameHandler:get_list(),
      [Username ||  Username<- List],
      message_loop(message);
    {get,ok} ->
      message_loop(message)
  end.

client() ->
  client_loop().

client_loop() ->       %client meesage loop handles the upcoming message to the client 
  receive
    {send, Pid, message} ->
      Pid ! {received,ok},
      confirm_message(),
      client_loop();
    {rest,Pid,message} ->
      Pid ! {get,ok},
      client_loop()

  end.


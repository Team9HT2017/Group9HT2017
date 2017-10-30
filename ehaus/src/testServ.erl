-module(testServ).

-export([start/0,loop/1]).

start() ->
  case whereis(testServ) of
    undefined ->
      P = spawn(fun () ->
        io:format("The server process is running!~n"),
        loop([])
                end),
      register(testServ, P),
      {ok, P};
    P -> {ok, P}
  end.

loop (Map) ->
  receive {IP,MapSim,put} ->
    {IP,'javaside@LAPTOP-7B292RJ1'} ! send_successful,
   % IP ! send_successful,
    loop(MapSim);
    {IP,get_map} ->
      {IP,'javaside@LAPTOP-7B292RJ1'} ! Map,
      loop(Map);
    {IP,stop} ->{IP,'jav aside@LAPTOP-7B292RJ1'} ! stopped
  end.





%%loop (Map) ->
%%  receive {IP,MapSim,put} ->
%%    IP ! send_successful,
%%    loop(MapSim);
%%    {IP,get_map} ->
%%      IP ! Map,
%%      loop(Map);
%%      {IP,stop} -> stopped
%%    end.

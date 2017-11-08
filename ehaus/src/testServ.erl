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
    {IP,'javaside@user-Dator'} ! send_successful,
   % IP ! send_successful,
    loop(MapSim);
    {IP,get_map} ->
      {IP,'javaside@user-Dator'} ! Map,
      loop(Map);
    {IP,stop} ->{IP,'jav aside@user-Dator'} ! stopped
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

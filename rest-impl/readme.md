
The reason we *don't* use switchyard here is because using dynamic dispatching with switchyard is complex.   

It's easier to just way easier to use RestEasy because of this -- although we then do lose the nice "plug & play" functionality that 
switchyard has that makes deployment/configuration easier.  
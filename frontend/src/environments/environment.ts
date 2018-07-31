export const environment = {
  production: true,
  javaServiceEndpoint: "wallet/",
  ourAddress : "accXpfvxnZa8txuxpjyPqzBaqYPHqYu2rwn34lL8rjI=",
  ourKey : "e+dQmxnWU+X9pTWLZI6GsCXQ1QH23+rRRGZOzUkM3k1pxxel+/Gdlry3G7GmPI+rMFqpg8epi7avCffiUvyuMg==",
  monitorUrlPart : "Account?id=",
  networks: [
    {name: "Production net", serviceAddress: "https://167.114.50.218:8443", isProd: true, monitorAddress: "https://monitor.credits.com"},
    {name: "Test net", serviceAddress: "https://167.114.50.218:8443", isProd: false, monitorAddress: "https://monitor.credits.com/test"}
  ]
};

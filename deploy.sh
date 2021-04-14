docker push innon/account-mi-server:latest
docker push innon/account-mi-client:latest

docker push innon/account-mi-server:$SHA
docker push innon/account-mi-client:$SHA

kubectl apply -f k8s
kubectl set image deployments/account-mi-server-deployment account-mi-server=innon/account-mi-server:$SHA
kubectl set image deployments/account-mi-client-deployment account-mi-client=innon/account-mi-client:$SHA
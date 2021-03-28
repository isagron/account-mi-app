FROM node:14.14.0-alpine
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build --prod


FROM nginx:alpine
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=0 /app/dist/accout-mi /usr/share/nginx/html
EXPOSE 80

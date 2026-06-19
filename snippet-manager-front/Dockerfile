FROM node:22-alpine AS builder
WORKDIR /app
COPY package.json .
COPY package-lock.json .
RUN npm ci

COPY vite.config.ts .
COPY vitest.config.ts .
COPY tsconfig.json .
COPY tsconfig.app.json .
COPY tsconfig.node.json .

COPY public ./public
COPY src ./src
COPY index.html .

RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
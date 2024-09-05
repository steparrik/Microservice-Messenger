const registerForm = document.querySelector('#register-form');
const newDialog = document.querySelector("#new-dialog");
const newGroup = document.querySelector("#new-group");
const loginForm = document.querySelector('#login-form');
const chatList = document.getElementById('chat-list');
const  errorAuth = document.querySelector("#error")
const chatName = document.querySelector("#chatName");
const chatPage = document.querySelector("#chat-page");
const messageArea = document.querySelector("#messageArea");
const messageInput = document.querySelector("#messageForm");
const logout = document.querySelector("#logout-button");
const profilePage = document.querySelector("#profile");
const profileData = document.querySelector("#profileData")
const forImg = document.querySelector("#forImg")
let stompClient;
const currentPath = window.location.pathname;
const jwt = localStorage.getItem("jwt");
const error = document.querySelector("#error-name")
const addNewParticipant = document.querySelector("#addNewParticipant")
const addParticipant = document.querySelector("#addParticipant")



document.addEventListener('DOMContentLoaded',  async function (event) {
    if (jwt && await getProfile() === 401) {
        localStorage.removeItem("jwt")
        window.location.href = '/auth';
    }
    else if (!jwt && currentPath !== '/register' && currentPath !== '/auth') {
        window.location.href = '/auth';
    } else if (jwt && (currentPath === '/register' || currentPath === '/auth')) {
        window.location.href = '/chats';
    }


    let stompClient;
    const subscriptions = {};

    if (chatList) {
        let stompClient = null;
        const subscriptions = {};

        function saveNotificationState(chatId, unreadCount) {
            localStorage.setItem(`notification_${chatId}`, JSON.stringify({ unreadCount }));
        }

        function restoreNotificationState() {
            document.querySelectorAll('.chat-item').forEach(chatItem => {
                const chatId = chatItem.getAttribute('data-chat-id');
                const notificationState = JSON.parse(localStorage.getItem(`notification_${chatId}`)) || { unreadCount: 0 };

                const notificationIcon = chatItem.querySelector('.notification-icon');
                const notificationText = chatItem.querySelector('.notification-text');

                if (notificationIcon) {
                    if (notificationState.unreadCount > 0) {
                        notificationIcon.style.display = 'block';
                        notificationText.textContent = notificationState.unreadCount;
                    } else {
                        notificationIcon.style.display = 'none';
                    }
                }
            });
        }

        function onMessageReceived(payload) {
            const message = JSON.parse(payload.body);
            const chatId = message.chatId;
            const senderName = message.sender.username;

            fetchChats();

            const chatItem = document.querySelector(`.chat-item[data-chat-id="${chatId}"]`);
            if (chatItem) {
                const notificationIcon = chatItem.querySelector('.notification-icon');
                const notificationText = chatItem.querySelector('.notification-text');

                if (notificationIcon) {
                    let unreadCount = parseInt(notificationText.textContent, 10) || 0;
                    unreadCount++;

                    notificationIcon.style.display = 'block';
                    notificationText.textContent = unreadCount;

                    saveNotificationState(chatId, unreadCount);
                }
            }
        }

        function onConnected() {
            console.log('Connected to WebSocket');
            fetchChats();
        }

        function onError(error) {
            console.error('Ошибка подключения к WebSocket:', error);
        }

        function subscribeToChat(chatId) {
            if (subscriptions[chatId]) {
                console.log(`Already subscribed to chat ${chatId}`);
                return;
            }

            const subscription = stompClient.subscribe('/topic/public/notification/' + chatId, onMessageReceived);
            subscriptions[chatId] = subscription;
            console.log(`Subscribed to chat ${chatId}`);
        }

        function unsubscribeFromChat(chatId) {
            if (subscriptions[chatId]) {
                subscriptions[chatId].unsubscribe();
                delete subscriptions[chatId];
                console.log(`Unsubscribed from chat ${chatId}`);
            }
        }

        function connect() {
            const socket = new SockJS('http://localhost:8081/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }

        function fetchChats() {
            fetch('http://localhost:8086/chats', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem("jwt")
                }
            })
                .then(response => {
                    if (!response.ok) {
                        const listItem = document.createElement('li');
                        listItem.textContent = "Вы не авторизованы";
                        chatList.appendChild(listItem);
                        return;
                    }
                    return response.json();
                })
                .then(data => {
                    if (data) {
                        chatList.innerHTML = '';
                        data.forEach(chat => {
                            const listItem = document.createElement('li');
                            listItem.setAttribute('data-chat-id', chat.id);
                            listItem.className = 'chat-item';

                            const chatLink = document.createElement('a');
                            chatLink.href = `chat?chatId=${chat.id}`;
                            chatLink.className = 'chat-link';

                            const chatIcon = document.createElement('div');
                            chatIcon.className = 'chat-icon';
                            chatIcon.textContent = chat.name.charAt(0).toUpperCase();

                            const notificationIcon = document.createElement('div');
                            const notificationText = document.createElement('span');
                            notificationIcon.className = 'notification-icon';
                            notificationText.className = 'notification-text';
                            notificationIcon.appendChild(notificationText);
                            notificationIcon.style.display = 'none';

                            const chatDetails = document.createElement('div');
                            chatDetails.className = 'chat-details';

                            const chatName = document.createElement('span');
                            chatName.className = 'chat-name';
                            chatName.textContent = chat.name;

                            const chatLastMessage = document.createElement('span');
                            chatLastMessage.className = 'chat-last-message';

                            chatDetails.appendChild(chatName);
                            chatDetails.appendChild(chatLastMessage);

                            chatLink.appendChild(chatIcon);
                            chatLink.appendChild(chatDetails);
                            listItem.appendChild(chatLink);
                            listItem.appendChild(notificationIcon);
                            chatList.appendChild(listItem);

                            subscribeToChat(chat.id);
                        });

                        restoreNotificationState();
                    }
                })
                .catch(error => console.error('Ошибка:', error));
        }

        document.addEventListener('click', function (event) {
            if (event.target.closest('.chat-item')) {
                const chatItem = event.target.closest('.chat-item');
                const chatId = chatItem.getAttribute('data-chat-id');

                const notificationIcon = chatItem.querySelector('.notification-icon');
                const notificationText = chatItem.querySelector('.notification-text');

                if (notificationIcon) {
                    notificationIcon.style.display = 'none';
                    saveNotificationState(chatId, 0);
                }
            }
        });

        if (localStorage.getItem("jwt")) {
            connect();
        } else {
            chatList.innerHTML = '<li>Вы не авторизованы</li>';
        }
    }



    if (newDialog) {
        newDialog.addEventListener("submit", async function (event) {
            const nameCompanion = document.querySelector("#nameCompanion").value
            console.log(nameCompanion)
            event.preventDefault()
            const newChatData = {
                chatType: "DIALOG"
            }
            const response = await addAndGetNewDialog(nameCompanion, JSON.stringify(newChatData));
            if (response === null) {
                console.log("error")
            } else {
                window.location.href = "chat?chatId=" + response.id
            }
        })
        newGroup.addEventListener("submit", async function (event) {
            const nameGroup = document.querySelector("#nameGroup").value
            if (nameGroup === null || nameGroup === "") {
                return
            }
            const newChatData = {
                chatType: "GROUP",
                name: nameGroup
            }
            event.preventDefault()
            const response = await addAndGetNewGroup(JSON.stringify(newChatData
            ))
            if (response === null) {
                console.log("error")
            } else {
                window.location.href = "chat?chatId=" + response.id

            }
        })
    }

    if (addParticipant) {
        const currentUrl = window.location.href;
        const url = new URL(currentUrl);
        const chatId = url.searchParams.get('chatId');

        addParticipant.addEventListener("submit", async function (event) {


            const nameCompanion = document.querySelector("#buttonForAddParticipant").value
            console.log(nameCompanion)
            event.preventDefault()
            const response = await addParticipants(nameCompanion, chatId);
            if (response !== null) {
                console.log("error")
            } else {
                window.location.href = "chat?chatId=" + chatId
            }
        })
    }



    if (chatPage) {
        const currentUrl = window.location.href;
        const url = new URL(currentUrl);
        const chatId = url.searchParams.get('chatId');

        function scrollToBottom() {
            messageArea.scrollTop = messageArea.scrollHeight;
        }

        function onError(error) {
            const connectingElement = document.getElementById('connecting');
            connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
            connectingElement.style.color = 'red';
        }

        async function sendMessage(event) {
            event.preventDefault();
            const messageContent = messageInput.querySelector("#message").value.trim();
            if (messageContent.trim() === "") {
                return
            }
            const currentUrl = window.location.href;
            const url = new URL(currentUrl);
            const chatId = url.searchParams.get('chatId');

            const message = await sendMessageOnServer(chatId, messageContent)

            if (messageContent && stompClient && stompClient.connected) {
                stompClient.send("/app/chat.sendMessage/" + chatId, {}, JSON.stringify(message));
            }
            messageInput.querySelector("#message").value = '';
        }

        function onConnected() {
            const currentUrl = window.location.href;
            const url = new URL(currentUrl);
            const chatId = url.searchParams.get('chatId');
            // Subscribe to the Public Topic
            stompClient.subscribe('/topic/public/' + chatId, onMessageReceived);
        }

        function onMessageReceived(payload) {
            const message = JSON.parse(payload.body);
            console.log(message.sender.username)
            const jwtPayload = parseJwt(localStorage.getItem("jwt"));
            const jwtUsername = jwtPayload.sub

            const messageContainer = document.createElement('div');

            const listItem = document.createElement('li');
            const messageText = document.createElement('div');
            if (jwtUsername !== message.sender.username) {
                listItem.classList = "message left"

                if(message.chatType === "GROUP"){
                    const chatI = document.createElement('div');
                    chatI.className = 'chat-icon';
                    chatI.textContent = message.sender.username.charAt(0).toUpperCase();
                    messageContainer.className = 'chat-link';
                    messageContainer.appendChild(chatI);
                    const senderNameMini = document.createElement("div")
                    senderNameMini.className = "senderNameMini"
                    senderNameMini.textContent = message.sender.fullName

                    listItem.appendChild(senderNameMini);
                }
            } else {
                listItem.classList = "message right"
                messageContainer.className = "rightM"
            }

            messageText.textContent =
                message.messageText;

            console.log(message.timestamp)

            const time = message.timestamp.toString().substring(11,16);

            messageText.className = 'message-text';

            const messageTime = document.createElement('div');
            messageTime.className = 'message-time';
            messageTime.textContent = time;


            listItem.appendChild(messageText);
            listItem.appendChild(messageTime);
            messageContainer.appendChild(listItem)
            messageArea.appendChild(messageContainer);
            scrollToBottom()
        }

        function connect() {
            const socket = new SockJS('http://localhost:8081/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }

        fetch(`http://localhost:8086/chats/` + chatId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem("jwt")
            }
        })
            .then(response => {
                if (!response.ok) {
                    const listItem = document.createElement('li');
                    listItem.textContent = "Вы не авторизованы";
                    chatList.appendChild(listItem);
                    return;
                }
                return response.json();
            })
            .then(data => {
                if (data) {
                    console.log(data);
                    const name = document.createElement('h1');
                    if(data.chatType === "GROUP"){
                        linkForAddNewParticipant = document.createElement("a");
                        linkForAddNewParticipant.href = "addNewParticipant?chatId="+chatId
                        linkForAddNewParticipant.textContent = "Добавить участника"
                        addNewParticipant.appendChild(linkForAddNewParticipant);
                    }
                    name.textContent = data.name.trim();
                    chatName.appendChild(name);
                    messageArea.innerHTML = '';
                    data.messages.forEach(message => {
                        const jwtPayload = parseJwt(localStorage.getItem("jwt"));
                        const jwtUsername = jwtPayload.sub

                        const listItem = document.createElement('li');
                        const messageText = document.createElement('div');
                        const messageContainer = document.createElement('div');

                        if (jwtUsername !== message.sender.username) {
                            listItem.classList = "message left"

                            if(data.chatType === "GROUP") {
                                const senderNameMini = document.createElement("div")
                                senderNameMini.className = "senderNameMini"
                                senderNameMini.textContent = message.sender.fullName
                                listItem.appendChild(senderNameMini);

                                const chatIcon = document.createElement('div');
                                chatIcon.className = 'chat-icon';
                                chatIcon.textContent = message.sender.username.charAt(0).toUpperCase();

                                messageContainer.appendChild(chatIcon)
                                messageContainer.className = 'chat-link'
                            }
                        } else {
                            listItem.classList = "message right"
                            messageContainer.className = 'rightM'
                        }
                        messageText.textContent =
                            message.messageText;

                        const time = message.timestamp.toString().substring(11,16);


                        messageText.className = 'message-text';

                        const messageTime = document.createElement('div');
                        messageTime.className = 'message-time';
                        messageTime.textContent = time;

                        listItem.appendChild(messageText);
                        listItem.appendChild(messageTime);
                        messageContainer.appendChild(listItem)
                        messageArea.appendChild(messageContainer);
                    });
                    scrollToBottom()
                }
            });
        connect();
        messageInput.addEventListener("submit", sendMessage, true);
    }

    if (profilePage) {
            const user = await getProfile();


            const profileLi = document.createElement('div');

            const username = document.createElement("a");
            username.textContent = "Никнейм: " + user.username;
            console.log(username)

            const pN = document.createElement("a");
            pN.textContent = "Номер телефона: " + user.phoneNumber;

            const fN = document.createElement("a");
            fN.textContent = "Полное имя: " + user.fullName;

            const pA = document.createElement("img")
            pA.src= user.pathToAvatar;

            profileLi.appendChild(username);
            profileLi.appendChild(document.createElement("br"))
            profileLi.appendChild(pN);
            profileLi.appendChild(document.createElement("br"))
            profileLi.appendChild(fN);
            profileLi.appendChild(document.createElement("br"))

            profileData.appendChild(profileLi);
            profileData.appendChild(pA)


            profilePage.addEventListener("submit", function (event) {
                    localStorage.removeItem("jwt")
                }
            )
    }

    if (registerForm) {
        const errorRegister = document.querySelector("#error")
        registerForm.addEventListener("submit", function (event) {
            const username = document.querySelector("#usernameR").value
            const password = document.querySelector("#passwordR").value
            const confirmPassword = document.querySelector("#passwordRepeatR").value
            const phoneNumber = document.querySelector("#phoneNumberR").value
            const fullName = document.querySelector("#fullNameR").value
            event.preventDefault();

            const RegisterData = {
                username: username,
                password: password,
                confirmPassword: confirmPassword,
                phoneNumber: phoneNumber,
                fullName: fullName,
            }
            fetch('http://localhost:8086/registration', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(RegisterData)
            })
                .then(async response => {
                    if (!response.ok) {
                        const errorMessage = await response.json()
                        console.log(errorMessage)
                        errorRegister.textContent = errorMessage.message
                    } else {
                        errorRegister.textContent = ""
                        window.location.href = "auth"
                    }
                })
        })
    }



    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            event.preventDefault();
            const username = document.querySelector('#username').value;
            const password = document.querySelector('#password').value;

            const authEntity = {
                username: username,
                password: password
            };

            fetch('http://localhost:8086/auth', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(authEntity)
            })
                .then(response => {
                    if (!response.ok) {
                        errorAuth.textContent = "Неверный логин или пароль";
                        return
                    }
                    return response.json();
                })
                .then(data => {
                    console.log(data);
                    localStorage.setItem("jwt", data.token);
                    window.location.href = "/chats"
                });
        });

    }

});

function getProfile() {
    return fetch('http://localhost:8086/profile', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("jwt")
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.status;
            }
            return response.json();
        })
        .then(data => {
            return data
        });
}


function sendMessageOnServer(chatId, text) {
    const messageText = {
        messageText: text
    }
    return fetch('http://localhost:8086/chats/'+chatId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("jwt")
        },
        body: JSON.stringify(messageText)
    })
        .then(response => {
            if (!response.ok) {
                const listItem = document.createElement('li');
                listItem.textContent = "Вы не авторизованы";
                chatList.appendChild(listItem);
                return;
            }
            return response.json();
        })
        .then(data => {
            return data
        });
}



function addAndGetNewDialog(nameCompanion, newChatData){
    return fetch('http://localhost:8086/chats' +
        '?username='+nameCompanion, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("jwt")
        }, body: newChatData
    })
        .then(async response => {
            if (!response.ok) {
                const as = await response.json();
                console.log(as.message
                )
                error.textContent = as.message
                return null
            }
            return response.json()
        })
        .then(data => {
            return data
        });
}

async function addAndGetNewGroup(groupName){
    return fetch('http://localhost:8086/chats', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("jwt")
        },body:groupName
    })
        .then(async response => {
            if(!response.ok){
                const as = await response.json();
                console.log(as.message)
                error.textContent = as.message
                return null
            }else{
                return response.json()}
        })
        .then(data => {
            return data
        });
}

function addParticipants(nameCompanion, chatId){
    return fetch('http://localhost:8086/chats/'+chatId +
        '?username='+nameCompanion, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("jwt")
        }
    })
        .then(async response => {
            if (!response.ok) {
                const as = await response.json();
                error.textContent = as.message
                return "error"
            }else{
                return null}
        })
}


function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    console.log(jsonPayload)
    return JSON.parse(jsonPayload);
}




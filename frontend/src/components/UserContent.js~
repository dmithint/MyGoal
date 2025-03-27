import React, {useState} from 'react';
import {request} from '../axios_helper';
import LoginForm from './LoginForm';
import UserProfile from "./UserProfile";
import {useAuth} from './AuthProvider';

const UserContent = () => {

    const {login, logout, isAuthenticated} = useAuth();

    const onLogin = (username, password) => {
        request("POST", "login", {login: username, password: password})
            .then((response) => {
                login(response.data.token, response.data.id);
            })
            .catch((error) => {
                logout();
            });
    };

    const onRegister = (firstName, lastName, username, password) => {
        console.log(firstName, lastName, username, password);
        request("POST", "register", {firstName: firstName, lastName: lastName, login: username, password: password})
            .then((response) => {
                console.log(response.data);
                login(response.data.token, response.data.id);
            })
            .catch((error) => {
                logout();
            });
    };

    return (
        <>
            <div style={{height: '70vh'}}>
                {(!isAuthenticated) ? <LoginForm onLogin={onLogin} onRegister={onRegister}/> : <UserProfile/>}
            </div>
        </>
    );
};

export default UserContent;
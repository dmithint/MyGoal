import {NavLink, useNavigate} from 'react-router-dom';
import {useAuth} from "../context/AuthProvider";
import '../styles/Header.css';

const UserHeader = ({user, handleAuthClick}) => (
    <>
        <nav className="nav-menu">
            <NavLink to="/profile" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Профиль
            </NavLink>
            <NavLink to="/trainings" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Тренировки
            </NavLink>
            <NavLink to="/history" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                История
            </NavLink>
            <NavLink to="/coaches" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Тренеры
            </NavLink>
        </nav>

        <div className="auth-section">
            <button onClick={handleAuthClick} className="auth-button logout">
                Выйти
            </button>
        </div>
    </>
);

const CoachHeader = ({user, handleAuthClick}) => (
    <>
        <nav className="nav-menu">
            <NavLink to="/profile" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Профиль
            </NavLink>
            <NavLink to="/trainings" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Тренировки
            </NavLink>
        </nav>

        <div className="auth-section">
            <button onClick={handleAuthClick} className="auth-button logout">
                Выйти
            </button>
        </div>
    </>
);

const AdminHeader = ({user, handleAuthClick}) => (
    <>
        <nav className="nav-menu">

            <NavLink to="/profile" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Профиль
            </NavLink>

            <NavLink to="admin/users" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Управление пользователями
            </NavLink>

            <NavLink to="admin/trainings" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Управление тренировками
            </NavLink>

            <NavLink to="admin/feedbacks" className={({isActive}) => isActive ? 'nav-item active' : 'nav-item'}>
                Управление отзывами
            </NavLink>

        </nav>

        <div className="auth-section">
            <button onClick={handleAuthClick} className="auth-button logout">
                Выйти
            </button>
        </div>
    </>
);

const Header = () => {
    const {user, logout} = useAuth();
    const navigate = useNavigate();

    const handleAuthClick = () => {
        user ? logout() : navigate("/");
    };

    const isAdmin = user?.roles?.includes("ADMIN");
    const isCoach = user?.roles?.includes("COACH");
    const isUser = user?.roles?.includes("ATHLETE");

    return (
        <header className="app-header">
            <div className="brand-container">
                <img
                    src={require("../assets/logo.png")}
                    alt="Fitness Logo"
                    className="brand-logo"
                />
                <span className="brand-text">MyGoal Fitness</span>
            </div>

            {!user ? (
                <div className="auth-section">
                    <button onClick={handleAuthClick} className="auth-button login">
                        Войти
                    </button>
                </div>
            ) : isAdmin ? (
                <AdminHeader user={user} handleAuthClick={handleAuthClick}/>
            ) : isCoach ? (
                <CoachHeader user={user} handleAuthClick={handleAuthClick}/>
            ) : isUser ? (
                <UserHeader user={user} handleAuthClick={handleAuthClick}/>
            ) : null}
        </header>
    );
};

export default Header;
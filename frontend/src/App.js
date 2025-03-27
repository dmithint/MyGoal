import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Header from "./components/Header";
// import Footer from "./components/Footer";
import UserContent from "./pages/UserContent";
import VisitHistory from "./pages/VisitHistory";
import Coaches from "./pages/Coaches";
import {Toaster} from "react-hot-toast";
import Trainings from "./pages/Trainings";
import {AuthProvider} from "./context/AuthProvider";
import UsersAdmin from "./pages/admin/UsersAdmin";
import TrainingsAdmin from "./pages/admin/TrainingsAdmin";
import FeedbacksAdmin from "./pages/admin/FeedbacksAdmin";

function App() {

    return (
        <>
            <AuthProvider>
                <Toaster position="bottom-left"/>
                <main className="main-content">
                    <Router>
                        <div>
                            <Header/>
                            <main>
                                <Routes>
                                    <Route path="/" element={<UserContent/>}/>
                                    <Route path="/profile" element={<UserContent/>}/>
                                    <Route path="/trainings" element={<Trainings/>}/>
                                    <Route path="/history" element={<VisitHistory/>}/>
                                    <Route path="/coaches" element={<Coaches/>}/>
                                    <Route path="/admin/feedbacks" element={<FeedbacksAdmin/>}/>
                                    <Route path="/admin/users" element={<UsersAdmin/>}/>
                                    <Route path="/admin/trainings" element={<TrainingsAdmin/>}/>
                                </Routes>
                            </main>
                            {/*<Footer/>*/}
                        </div>
                    </Router>
                </main>
            </AuthProvider>
        </>
    )
        ;
}

export default App;

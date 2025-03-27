export async function loginRequest(email, password) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({
                firstName: "Egor",
                lastName: "Letton",
                email: "sobaka@mail.ru",
                roles: ["SPORTSMAN"],
                token: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkbWl0ci5rcnl6aEBnbWFpbC5jb20iLCJyb2xlcyI6WyJDTElFTlQiXSwiaWQiOjEsImV4cCI6MTc0MjE2NDI4OSwiaWF0IjoxNzQyMDc3ODg5fQ.EcylKNTMHt78gfPEJeBhQcQy4yGATbvJwylLFfAmlug",
            });
        }, 1000);
    });
}

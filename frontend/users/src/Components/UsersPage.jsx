import { useEffect, useState } from "react";
import {Typography, Box, Grid2} from "@mui/material";
import UserCard from "./UserCard";
import { getUsers } from "../Utility/call-utility";

export default () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        if (!users.length) {
            getUsers()
                .then((r) => setUsers(r.data))
                .catch(() => alert("Not Loggged in"));
        }
    }, [users]);

    return (
        <Box sx={{ padding: 12, backgroundColor: "#f5f5f5", minHeight: "100vh" }}>
            <Typography
                variant="h2"
                sx={{ fontWeight: "bold", color: "#d22d41", mb: 3, textTransform: "uppercase", letterSpacing: 2 }}
            >
                For You
            </Typography>
            <Grid2 container spacing={30} justifyContent="center">
                {users.map((user) => (
                    <Grid2 item xs={12} sm={6} md={4} lg={3} sx={{ transform: "scale(2)" }} key={user.username}>
                        <UserCard user={user} />
                    </Grid2>
                ))}
            </Grid2>
        </Box>
    );
};

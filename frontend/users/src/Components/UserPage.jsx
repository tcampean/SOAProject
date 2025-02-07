import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import {
    Avatar,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    FormControl,
    FormLabel,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {
    createPost,
    deletePost,
    subscribeToUser,
    getCurrentUser,
    getUser,
    getUserSubscribers,
    getSubscribedUser,
    getUserPosts,
    unsubscribeUser
} from "../Utility/call-utility";
import { useSubscription } from "react-stomp-hooks";

function UserPage() {
    const [user, setUser] = React.useState(null);
    const [posts, setPosts] = React.useState([]);
    const [subscribers, setSubscribers] = React.useState([]);
    const [subscribing, setSubscribing] = React.useState([]);
    const [currentUser, setCurrentUser] = React.useState(null);
    const { username } = useParams();

    useSubscription("/topic/post-" + username, (message) => {
        if (currentUser !== null && currentUser.username !== username) {
            alert(message.body + "\nRefresh if you want to see it now!");
        }
    });

    useEffect(() => {
        if (user === null) {
            getUser(username)
                .then((r) => setUser(r.data))
                .catch((response) => alert("Please log in first!"));
        }
        if (!posts.length) {
            getUserPosts(username, true)
                .then((r) => setPosts(r.data))
                .catch((response) => alert("Please log in first!"));
        }
        if (!subscribers.length) {
            getUserSubscribers(username)
                .then((r) => setSubscribers(r.data))
                .catch((response) => alert("Please log in first!"));
        }
        if (!subscribing.length) {
            getSubscribedUser(username)
                .then((r) => setSubscribing(r.data))
                .catch((response) => alert("Please log in first!"));
        }
        if (currentUser === null) {
            getCurrentUser()
                .then((r) => setCurrentUser(r.data))
                .catch((response) => alert("Please log in first!"));
        }
    }, [username]);

    const handleSubscription = () => {
        if (
            subscribers.findIndex(
                (subscriber) => currentUser !== null && currentUser.username === subscriber.username
            ) !== -1
        ) {
            unsubscribeUser(username).then((r) => window.location.reload());
        } else {
            subscribeToUser(username).then((r) => window.location.reload());
        }
    };

    const handlePost = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        createPost(data.get("title"), data.get("body"))
            .then((r) => window.location.reload())
            .catch((response) => alert("Please log in first!"));
    };

    const handleDeletePost = (id) => {
        deletePost(id)
            .then((r) => window.location.reload())
            .catch((response) => alert("Please log in first!"));
    };

    return (
        <Stack direction="row" sx={{ display: "flex", justifyContent: "space-between", marginTop: "10px" }}>
            <Stack
                direction="column"
                sx={{
                    width: "25%",
                    padding: "30px",
                    backgroundColor: "#d22d41",
                    borderRadius: "10px",
                    boxShadow: 5,
                    position: "sticky",
                    top: "20px",
                    color: "#fff",
                }}
            >

                <Box sx={{ textAlign: "center", marginBottom: "30px" }}>
                    <Avatar sx={{ width: 64, height: 64}} src={"/default-avatar.png"} />
                    <Typography variant="h4" sx={{ fontWeight: "bold" }}>
                        {user?.username}
                    </Typography>
                    <Typography variant="h6">Subscribers: {subscribers.length}</Typography>
                    <Typography variant="h6">Subscribed: {subscribing.length}</Typography>
                </Box>

                {currentUser !== null && currentUser.username !== username && (
                    <Button
                        variant="contained"
                        fullWidth
                        sx={{
                            backgroundColor: "#fff",
                            color: "#3f51b5",
                            "&:hover": { backgroundColor: "#eeeeee" },
                        }}
                        onClick={handleSubscription}
                    >
                        {subscribers.findIndex(
                            (subscriber) => currentUser !== null && currentUser.username === subscriber.username
                        ) !== -1
                            ? "Unsubscribe"
                            : "Subscribe"}
                    </Button>
                )}

                {currentUser !== null && currentUser.username === username && (
                    <Card sx={{ marginTop: "20px", backgroundColor: "#fff", color: "#d22d41" }}>
                        <CardContent>
                            <Typography variant="h5">Create New Post</Typography>
                            <Box
                                component="form"
                                onSubmit={handlePost}
                                sx={{
                                    display: "flex",
                                    flexDirection: "column",
                                    gap: 2,
                                    marginTop: "20px",
                                }}
                            >
                                <FormControl>
                                    <FormLabel htmlFor="title" sx={{ fontSize: "1.2rem" }}>Post Title</FormLabel>
                                    <TextField
                                        id="title"
                                        type="text"
                                        name="title"
                                        placeholder="Title"
                                        required
                                        fullWidth
                                        variant="outlined"
                                        sx={{ fontSize: "1.1rem" }}
                                    />
                                </FormControl>
                                <FormControl>
                                    <FormLabel htmlFor="body" sx={{ fontSize: "1.2rem" }}>Text Body</FormLabel>
                                    <TextField
                                        name="body"
                                        placeholder="Post Content"
                                        type="text"
                                        multiline
                                        id="body"
                                        required
                                        fullWidth
                                        minRows={5}
                                        variant="outlined"
                                        sx={{ fontSize: "1.1rem" }}
                                    />
                                </FormControl>
                                <Button type="submit" fullWidth variant="contained" sx={{ marginTop: "15px", fontSize: "1.5rem", backgroundColor: "#d22d41" }}>
                                    Create Post
                                </Button>
                            </Box>
                        </CardContent>
                    </Card>
                )}
            </Stack>

            <Stack
                direction="column"
                sx={{
                    width: "70%",
                    overflowY: "auto",
                    maxHeight: "calc(100vh - 150px)",
                    padding: "20px",
                }}
            >
                {posts.length === 0 && <Typography variant="h4">No Posts</Typography>}
                {posts.map((post) => (
                    <Card
                        key={post.id}
                        sx={{
                            marginTop: "20px",
                            padding: "20px",
                            boxShadow: 3,
                        }}
                    >
                        <CardContent>
                            <Typography variant="h3">{post.title}</Typography>
                            <Box
                                sx={{
                                    "& h1": { fontSize: "3.5rem" },
                                    "& h2": { fontSize: "3rem" },
                                    "& h3": { fontSize: "2.5rem" },
                                    "& h4": { fontSize: "2.2rem" },
                                    "& h5": { fontSize: "2rem" },
                                    "& h6": { fontSize: "1.8rem" },
                                }}
                                component="div"
                            >
                                <article dangerouslySetInnerHTML={{ __html: post.content }}></article>
                            </Box>
                        </CardContent>
                        <CardActions>
                            {currentUser !== null && currentUser.username === username && (
                                <Button onClick={() => handleDeletePost(post.id)} sx={{ fontSize: "1.1rem", color: "#d22d41" }}>Delete Post</Button>
                            )}
                        </CardActions>
                    </Card>
                ))}
            </Stack>
        </Stack>
    );
}

export default UserPage;

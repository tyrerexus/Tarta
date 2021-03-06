import { Component } from 'inferno';
import { connect } from 'inferno-redux';
import { Link } from 'inferno-router';
import { fetchHTTP } from '../../helpers/fetch_http';

class FeedPage extends Component<any, {
    feed: Post[] | null
}> {
    constructor() {
        super();
        this.state = {
            feed: null,
        };
    }

    componentDidMount() {
        this.fetchFeed();
    }
    
    fetchFeed() {
        const sessionID = this.props.session;
        fetchHTTP<[Post]>('http://localhost:3000/posts/feed/get', {sessionID})
        .then(feed => this.setState({feed}));
    }
    render() {
        if (this.state === null) {
            return null;
        }

        const { feed } = this.state;
        console.log(feed);

        return (
            <div className="feed-page panel">
                <div>
                    {feed !== null ? (
                        feed.map(post => (
                            <div>
                                <h3>{post.title}</h3>
                                {post.content}
                                <br />
                                {'Recipient: ' + post.recipient.name}
                                <hr />
                            </div>
                        ))
                    ) : (
                        'Loading ...'
                    )}
                </div>
                <Link to="/create">{'Create a new post.'}</Link>
            </div>
        )
    }
}

export default connect((state:any) => ({
    session: state.session,
}))(FeedPage);
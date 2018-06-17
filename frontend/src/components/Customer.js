import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Redirect} from 'react-router';
import moment from 'moment';
import {STATUS_EXPLANATION_MAP} from './Constants';

class Customer extends Component {
    static propTypes = {
        customer: PropTypes.shape({
            id: PropTypes.number.isRequired,
            name: PropTypes.string.isRequired,
            status: PropTypes.string,
            createdAt: PropTypes.string,
        }).isRequired
    };

    state = {
        redirect: false,
    };
    handleDetail = () => this.setState({redirect: true});

    render() {
        const {id,name, status, createdAt} = this.props.customer;

        if(this.state.redirect) {
            return <Redirect push to={`/detail/${id}`} />;
        }
        return (
            <tr>
                <td>{name}</td>
                <td>{STATUS_EXPLANATION_MAP[status]}</td>
                <td>{moment(createdAt).format('MMMM Do YYYY, h:mm:ss a')}</td>
                <td>
                    <button className="btn btn-info" onClick={this.handleDetail}>Detail</button>
                </td>
            </tr>
        );
    }
}

export default Customer;

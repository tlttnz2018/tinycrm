import React, {Component} from 'react';
import PropTypes from 'prop-types';

class Note extends Component {
    static propsTypes = {
        note: PropTypes.shape({
            id: PropTypes.string.isRequired,
            nodeSubject: PropTypes.string.isRequired,
            nodeBody: PropTypes.string
        }).isRequired
    };

    editNote = () => this.props.onStartEdit();

    render() {
        const {id, noteSubject, noteBody} = this.props.note;
        return (
            <div className="card mb-2">
                <div className="card-header" id={`heading_${id}`}>
                    <h5 className="mb-0">
                        <button className="btn btn-link" data-toggle="collapse" data-target={`#collapse_${id}`}
                                aria-expanded="true" aria-controls={"collapse_" + id}>
                            {noteSubject}
                        </button>
                        <button type="button" className="btn btn-primary float-right" onClick={this.editNote}>Edit</button>
                    </h5>
                </div>

                <div id={`collapse_${id}`} className="collapse" aria-labelledby={`heading_${id}`} data-parent="#accordion">
                    <div className="card-body">
                        {noteBody}
                    </div>
                </div>
            </div>
        );
    }
}

export default Note;

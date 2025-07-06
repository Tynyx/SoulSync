// src/components/Recommendations.jsx
import React, {useEffect, useState} from 'react'
import PropTypes from 'prop-types'

/**
 * Props:
 * - all: Anime[]        // full catalog
 * - watched: Anime[]    // user’s watched list
 * - recommend: (all, watched, n) => Anime[]
 */

export default function Recommendations({ all, watched, onRecommend }) {
    // run the recommendation algorithm once on mount
    useEffect(() => {
        onRecommend();
    }, []);

    if (!watched.length) {
        return (
            <p className="text-gray-600">
                No recommendations yet. Upload a list or add some anime first!
            </p>
        );
    }

    return (
        <div className="space-y-2">
            <h2 className="text-2xl font-bold">Top {watched.length} Recommendations</h2>
            <ul className="divide-y">
                {watched.map(a => (
                    <li key={a.id} className="py-2">
                        <span className="font-semibold">{a.title}</span>{' '}
                        <span className="text-sm text-gray-500">({a.rating.toFixed(1)})</span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

Recommendations.propTypes = {
    all: PropTypes.array.isRequired,
    watched: PropTypes.array.isRequired,
    onRecommend: PropTypes.func.isRequired,
};
// src/components/AnimeList.jsx
import React from 'react'
import PropTypes from 'prop-types'

export default function AnimeList({ list }) {
    if (!list.length) {
        return <p className="text-cyan-200">No anime loaded yet — use “Upload TXT” or “Add Anime.”</p>
    }

    return (
        <div className="overflow-x-auto bg-cyan-900/20 p-4 rounded-lg drop-shadow-lg">
            <table className="min-w-full bg-transparent text-cyan-200">
                <thead>
                <tr className="border-b border-cyan-400/60">
                    {['ID','Title','Genre','Watched','Total','Status','Rating'].map(header => (
                        <th
                            key={header}
                            className="px-4 py-2 text-left font-semibold uppercase tracking-wide text-cyan-300"
                        >
                            {header}
                        </th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {list.map(a => (
                    <tr
                        key={a.id}
                        className="odd:bg-cyan-900/30 even:bg-cyan-900/15 hover:bg-cyan-800/40 transition"
                    >
                        <td className="px-4 py-2">{a.id}</td>
                        <td className="px-4 py-2">{a.title}</td>
                        <td className="px-4 py-2">{a.genre}</td>
                        <td className="px-4 py-2">{a.epsWatched}</td>
                        <td className="px-4 py-2">{a.totalEps}</td>
                        <td className="px-4 py-2">{a.status}</td>
                        <td className="px-4 py-2">{a.rating}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

AnimeList.propTypes = {
    list: PropTypes.arrayOf(PropTypes.shape({
        id:        PropTypes.number,
        title:     PropTypes.string,
        genre:     PropTypes.string,
        epsWatched:PropTypes.number,
        totalEps:  PropTypes.number,
        status:    PropTypes.string,
        rating:    PropTypes.number
    })).isRequired
}

import React, { useState } from 'react'
import PropTypes from 'prop-types'

export default function UpdateAnimeForm({ onUpdate }) {
    const [title, setTitle]         = useState('')
    const [newWatched, setNewWatched] = useState('')
    const [newTotal, setNewTotal]     = useState('')
    const [newStatus, setNewStatus]   = useState('')
    const [newRating, setNewRating]   = useState('')

    function handleSubmit(e) {
        e.preventDefault()
        const patch = {}
        if (newWatched !== '') patch.epsWatched = Number(newWatched)
        if (newTotal   !== '') patch.totalEps   = Number(newTotal)
        if (newStatus  !== '') patch.status     = newStatus
        if (newRating  !== '') patch.rating     = Number(newRating)

        onUpdate(title, patch)

        setTitle(''); setNewWatched(''); setNewTotal(''); setNewStatus(''); setNewRating('')
    }


    return (
        <div className="flex items-center justify-center py-12">
            <form
                onSubmit={handleSubmit}
                className="mx-auto mt-8 max-w-md p-6 bg-cyan-900/30 rounded-lg space-y-6 text-cyan-100"
            >
                <h2 className="text-2xl text-center text-cyan-300 font-semibold">Update Anime</h2>

                <div className="flex flex-col">
                    <label className="block text-cyan-300">Title to Update</label>
                    <input
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        required
                        className="w-full rounded border border-cyan-500 p-2 bg-transparent text-cyan-100 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                        placeholder="Exact Title"
                    />
                </div>

                <div className="grid grid-cols-2 gap-4">
                    {[
                        ['New watched eps', newWatched, setNewWatched, 'number'],
                        ['New total eps',   newTotal,   setNewTotal,   'number'],
                    ].map(([lbl, val, set, tp]) => (
                        <div key={lbl} className="flex flex-col">
                            <label className="mb-1 text-cyan-200">{lbl}</label>
                            <input
                                type={tp}
                                value={val}
                                onChange={e => set(e.target.value)}
                                className="px-3 py-2 bg-transparent border border-cyan-700 rounded text-cyan-100 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                placeholder="Leave blank to skip"
                            />
                        </div>
                    ))}
                </div>

                {[
                    ['New status', newStatus, setNewStatus, 'text'],
                    ['New rating', newRating, setNewRating, 'number'],
                ].map(([lbl, val, set, tp]) => (
                    <div key={lbl} className="flex flex-col">
                        <label className="mb-1 text-cyan-200">{lbl}</label>
                        <input
                            type={tp}
                            value={val}
                            onChange={e => set(e.target.value)}
                            className="px-3 py-2 bg-transparent border border-cyan-700 rounded text-cyan-100 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                            placeholder="Leave blank to skip"
                        />
                    </div>
                ))}

                <button
                    type="submit"
                    className="w-full py-2 bg-cyan-300 text-cyan-900 font-medium rounded-lg hover:bg-cyan-200 transition"
                >
                    Update Anime
                </button>
            </form>
        </div>
    );
}
import React, { useState } from 'react'
import PropTypes from 'prop-types'

export default function AddAnimeForm({ onAdd }) {
    const [title, setTitle]       = useState('')
    const [genre, setGenre]       = useState('')
    const [epsWatched, setEps]    = useState('')
    const [totalEps, setTotal]    = useState('')
    const [status, setStatus]     = useState('')
    const [rating, setRating]     = useState('')

    function handleSubmit(e) {
        e.preventDefault()
        onAdd({
            title,
            genre,
            epsWatched: Number(epsWatched),
            totalEps:   Number(totalEps),
            status,
            rating:     Number(rating)
        })
        setTitle(''); setGenre(''); setEps(''); setTotal(''); setStatus(''); setRating('')
    }

    return (
        <div className="flex justify-center mt-12">
            <form
                onSubmit={handleSubmit}
                className="w-full max-w-md p-6 bg-cyan-900/30 backdrop-blur-lg rounded-lg space-y-6 text-cyan-100"
            >
                <h2 className="text-3xl font-semibold text-center text-cyan-300">
                    Add New Anime
                </h2>

                {[
                    { label: 'Title',      val: title,    setter: setTitle,   type: 'text'   },
                    { label: 'Genre',      val: genre,    setter: setGenre,   type: 'text'   },
                    { label: 'Watched eps',val: epsWatched, setter: setEps,    type: 'number' },
                    { label: 'Total Eps',  val: totalEps, setter: setTotal,   type: 'number' },
                    { label: 'Status',     val: status,   setter: setStatus,  type: 'text'   },
                    { label: 'Rating',     val: rating,   setter: setRating,  type: 'number', step: 0.1 },
                ].map(({label,val,setter,type,step}) => (
                    <div key={label} className="flex flex-col">
                        <label className="text-cyan-300 mb-1">{label}</label>
                        <input
                            required
                            type={type}
                            step={step}
                            value={val}
                            onChange={e => setter(e.target.value)}
                            className="p-2 rounded border border-cyan-500 bg-transparent text-cyan-100 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                        />
                    </div>
                ))}

                <button
                    type="submit"
                    className="w-full py-2 font-medium text-cyan-900 bg-cyan-300 hover:bg-cyan-200 rounded-lg transition"
                >
                    Add Anime
                </button>
            </form>
        </div>
    )
}

AddAnimeForm.propTypes = {
    onAdd: PropTypes.func.isRequired
}

// src/context/SoulSyncContext.jsx
import React, { createContext, useContext, useState } from 'react'
import { loadAnimeFile, addAnime, updateAnime, deleteAnime, recommendAnime } from '../utils/watchListManager'

const SoulSyncCtx = createContext()

export function SoulSyncProvider({ children }) {
    const [animeList, setAnimeList] = useState([])
    const [watchedList, setWatchedList] = useState([])

    // 1) load
    function handleLoad(text) {
        const loaded = loadAnimeFile(text)
        setAnimeList(loaded)
    }

    // 2) add
    function handleAdd(data) {
        const newRec = addAnime(animeList, data)
        setAnimeList([ ...animeList, newRec ])
    }

    // 3) update
    function handleUpdate(title, patch) {
        if (updateAnime(animeList, title, patch)) {
            setAnimeList([ ...animeList ])
        }
    }

    // 4) delete
    function handleDelete(title) {
        if (deleteAnime(animeList, title)) {
            setAnimeList([ ...animeList ])
            return true
        }
        return false
    }

    // 5) recommend
    function handleRecommend(N = 3) {
        const recs = recommendAnime(animeList, watchedList, N)
        setWatchedList(recs)
        return recs
    }

    return (
        <SoulSyncCtx.Provider value={{
            animeList,
            watchedList,
            handleLoad,
            handleAdd,
            handleUpdate,
            handleDelete,
            handleRecommend
        }}>
            {children}
        </SoulSyncCtx.Provider>
    )
}

export function useSoulSync() {
    return useContext(SoulSyncCtx)
}

// src/utils/watchListManager.js

/**
 * @typedef {{id:number,title:string,genre:string,epsWatched:number,totalEps:number,status:string,rating:number}} Anime
 */

/**
 * Purely parse a block of CSV-style text into Anime records.
 * Does not assign IDs—caller must decide where to insert them.
 *
 * @param {string} text  Raw file contents (each line: title,genre,epsWatched,totalEps,status,rating)
 * @returns {Omit<Anime,'id'>[]}  Parsed records without IDs
 */
export function parseAnimeText(text) {
    return text
        .split(/\r?\n/)
        .map(line => line.trim())
        .filter(line => !!line)
        .map(line => {
            const parts = line.split(',').map(s => s.trim());
            if (parts.length !== 6) return null;
            const [ title, genre, epsStr, totStr, status, ratingStr ] = parts;
            const epsWatched = Number.parseInt(epsStr, 10);
            const totalEps   = Number.parseInt(totStr,  10);
            const rating     = Number.parseFloat(ratingStr);
            if (
                !title || !genre || !status ||
                Number.isNaN(epsWatched) ||
                Number.isNaN(totalEps)   ||
                Number.isNaN(rating)
            ) return null;
            return { title, genre, epsWatched, totalEps, status, rating };
        })
        .filter(x => x !== null);
}


/**
 * @param {string} text
 * @returns {Anime[]}
 */
export function loadAnimeFile(text) {
    const out = []
    const lines = text.split(/\r?\n/).map(l => l.trim()).filter(l=>l.length)

    // If the first line contains non-numeric fields, assume it's a header
    const firstCols = lines[0]?.split(',')
    const hasHeader = firstCols && firstCols.some(c => isNaN(Number(c)))
    const dataLines = hasHeader ? lines.slice(1) : lines

    for (let line of dataLines) {
        const cols = line.split(',').map(c => c.trim())
        if (cols.length !== 6) continue
        const [ title, genre, epsWatched, totalEps, status, rating ] = cols
        const eWatched = parseInt(epsWatched,10),
            tEps     = parseInt(totalEps,10),
            rate     = parseFloat(rating)
        if ( Number.isNaN(eWatched) || Number.isNaN(tEps) || Number.isNaN(rate) ) continue
        out.push({
            id: out.length + 1,
            title,
            genre,
            epsWatched: eWatched,
            totalEps: tEps,
            status,
            rating: rate
        })
    }

    return out
}

/**
 * Add a single Anime record to your list (mutates), assigning a unique ID.
 *
 * @param {Anime[]} list
 * @param {Omit<Anime,'id'>} animeData
 * @returns {Anime} The newly created record (also pushed into list)
 */

export function addAnime(list, animeData) {
    const nextId = list.length
        ? Math.max(...list.map(a => a.id)) + 1
        : 1;
    const anime = { id: nextId, ...animeData };
    return [...list, anime];       // build & return a fresh array
}


/**
 * Update the first record whose title matches (case-insensitive).
 *
 * @param {Anime[]} list
 * @param {string} title
 * @param {Partial<Omit<Anime,'id'>>} patch
 * @returns {boolean} true if found & updated
 */
export function updateAnime(list, title, patch) {
    const idx = list.findIndex(a => a.title.toLowerCase() === title.toLowerCase());
    if (idx < 0) return false;
    list[idx] = { ...list[idx], ...patch };
    return true;
}

/**
 * Delete the first record whose title matches (case-insensitive).
 *
 * @param {Anime[]} list
 * @param {string} title
 * @returns {boolean} true if found & removed
 */
export function deleteAnime(list, title) {
    const idx = list.findIndex(a => a.title.toLowerCase() === title.toLowerCase());
    if (idx < 0) return false;
    list.splice(idx, 1);
    return true;
}

/**
 * Recommend up to N records based on watched history:
 * 1) If watched is empty → top-N by rating
 * 2) Else → most frequent genre, then top-N unseen in that genre
 *    (or fallback to top-rated overall if fewer)
 *
 * @param {Anime[]} all
 * @param {Anime[]} watched
 * @param {number} N
 * @returns {Anime[]}
 */
export function recommendAnime(all, watched, N) {
    if (!watched.length) {
        return all
            .slice()
            .sort((a,b) => b.rating - a.rating)
            .slice(0, N);
    }

    // tally genre frequencies
    const freq = {};
    watched.forEach(a => { freq[a.genre] = (freq[a.genre]||0) + 1 });

    // pick most-watched genre
    const favGenre = Object.entries(freq)
        .sort(([,c1],[,c2]) => c2 - c1)[0][0];

    // unseen in that genre
    const candidates = all.filter(a =>
        a.genre === favGenre &&
        !watched.some(w => w.id === a.id)
    );

    if (candidates.length >= N) {
        return candidates
            .sort((a,b) => b.rating - a.rating)
            .slice(0, N);
    }

    // fallback to top-rated overall (excluding already watched)
    return all
        .filter(a => !watched.some(w => w.id === a.id))
        .sort((a,b) => b.rating - a.rating)
        .slice(0, N);
}

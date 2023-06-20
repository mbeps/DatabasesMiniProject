-- Query 1
SELECT DISTINCT UniqueCarrier, COUNT(uniquecarrier)
FROM "delayedflights"
WHERE (depdelay > 0 OR arrdelay > 0)
GROUP BY uniquecarrier
ORDER BY COUNT(uniquecarrier) DESC 
LIMIT 5;

-- Query 2
SELECT DISTINCT airports.city, COUNT(depdelay)
FROM "airports"
JOIN "delayedflights" ON airports.airportcode = delayedflights.dest
WHERE delayedFlights.depdelay > 0
GROUP BY airports.city
ORDER BY COUNT(depdelay) DESC
limit 5;

-- Query 3
SELECT DISTINCT dest, SUM(arrdelay)
FROM "delayedflights"
GROUP BY dest
ORDER BY SUM(arrdelay) DESC
LIMIT 5 OFFSET 1;

-- Query 4
SELECT DISTINCT *
FROM "airports"
JOIN "delayedflights" ON airports.airportcode = delayedFlights.orig AND airports.airportcode = delayedFlights.orig
WHERE delayedflights.depdelay > 0 OR delayedflights.arrdelay > 0 -- departure state = arrival state
GROUP BY airports.state
ORDER BY COUNT(airports.state) DESC
;

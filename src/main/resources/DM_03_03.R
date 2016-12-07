install.packages("arules")
library(arules)

# Read CSV
states <- read.csv("/Users/Shaheen/Downloads/Clustering/bigml_582adbcc49c4a173fd000c1f.csv", header = T)
colnames(states)

# Save numerical data only
st <- states[, 4:9]
row.names(st) <- states[, 2]
colnames(st)



# CLUSTERING ###############################################

# Create distance matrix
d <- dist(st)

# Hierarchical clustering//////////
c <- hclust(d)
c # Info on clustering

# Plot dendrogram of clusters
plot(c, main = "Cluster with WEATHER, DRUNK_DR, FATALS, HIT_RUN, NO_LANES, SP_LIMIT, LAT, LNG")

# Or nest commands in one line (for sports data)
plot(hclust(dist(sports)), main = "WEATHER, DRUNK_DR, FATALS, HIT_RUN, NO_LANES, SP_LIMIT, LAT, LNG")


# Big Data Analytics and Recommendation System

This project implements a **scalable document ranking and recommendation system** designed to address the limitations of traditional keyword-based search engines.  
By leveraging **Apache Hadoop** for distributed processing and **sentence embeddings** for semantic similarity, the system provides **context-aware search results** from massive document repositories.

---

## Motivation
Conventional Boolean search models suffer from poor flexibility and ranking quality, as they rely on direct keyword matches without understanding semantic relationships. With the explosive growth of unstructured data, there is a critical need for **context-sensitive ranking models** that improve retrieval effectiveness.  

This project shifts the paradigm of search towards **semantic similarity-based ranking**, improving user experience and relevance across large-scale document libraries.

---

## Problem Statement
- Traditional search engines only match **keywords**, failing to capture **contextual meaning**.  
- Results may include irrelevant documents while missing conceptually related ones.  
- With rapidly expanding repositories, conventional models **struggle with scalability and efficiency**.  

**Goal:** Develop a **semantic search and recommendation system** that uses **sentence embeddings** and **TF-IDF weighting** to rank documents based on conceptual similarity, not just keyword overlap.  

---

## Methodology
1. Convert documents and queries into **high-dimensional embeddings** to capture semantic meaning.  
2. Use **TF-IDF** with **cosine similarity** for ranking document relevance.  
3. Employ **Apache Hadoop** to handle distributed computation and large-scale processing.  
4. Retrieve and rank the top N most relevant documents for user queries.  

---

## Experimental Setup
- Data stored and processed in **HDFS** (Hadoop Distributed File System).  
- **Apache Hadoop in Docker** used for scalable distributed computation.  
- **Workflow**:
  - Load `.txt` documents into HDFS.  
  - Mapper: Tokenizes text → generates `(word, 1)`.  
  - Reducer: Aggregates term frequencies (TF).  
  - Second Mapper: Computes inverse document frequency (IDF).  
  - TF-IDF vectors created and stored in HDFS.  
  - Query processed into a TF-IDF vector.  
  - **Cosine similarity** used to compare query vs. documents.  
  - Results sorted → top N documents retrieved with ID, title, and rank.  

---

## Workflow Overview
1. Upload documents into HDFS.  
2. Preprocess with **MapReduce** (TF-IDF computation).  
3. Build **inverted index** for document-term mapping.  
4. Accept user query and compute its TF-IDF representation.  
5. Compare query vector with document vectors using **cosine similarity**.  
6. Rank and return top N most relevant documents.  


---

## Key Highlights
- **Semantic search** with embeddings.  
- **Distributed processing** using Hadoop + Docker.  
- **Efficient TF-IDF ranking** with cosine similarity.  
- **Improved user experience** in large-scale search applications.  

# whatIsThisBird

My motivation for this project was to find what bird I was looking at. The goal was to take a picture of the bird, uplodad it to a site and have the application return the best matches based on color.

Backend is all Java, and the database is Redis. Front end is HTML/Javascript and AJAX. It needs to run on a webserver like Tomcat

Initial Bird database can be found here: http://www.vision.caltech.edu/visipedia/CUB-200-2011.html

How the backend works:
    
    - After the image is uploaded and sent to the backend via an AJAX call, the background is removed via Image Filtering: 
    the code for the filtering was found here: http://www.jhlabs.com/ip/filters/ I modified a few of the filters and they are part of the git package
    
    - After the background is removed we read the red, green and blue histograms of the image. They get put in three 255 int arrays. 
    
    - We then proceed to read all 36000 histograms ( 3 histograms x 12000 images) from the dataset which are in the Redis database. We run a correlation on the 3 histograms from the images versus the 36000 histograms from the dataset, and we select the images with the highest correlation. 
    
    - We then proceed to return those images to the client so it can display them. 
    


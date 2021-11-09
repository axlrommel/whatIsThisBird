# whatIsThisBird

My motivation for this project was to find what bird I was looking at. The goal was to take a picture of the bird, uplodad it to a site and have the application return the best matches based on color.

Backend is all Java, and the database is Redis. Front end is HTML/Javascript using AJAX. It needs to run on a webserver like Tomcat

Bird database pictures are found here: http://www.vision.caltech.edu/visipedia/CUB-200-2011.html

## How the backend works:
    
    - After the image is uploaded and sent to the backend via an AJAX call, the background is removed via Image Filtering: 
    the code for the filtering was found here: http://www.jhlabs.com/ip/filters/ I modified a few of the filters and they are part of the git package. Modifications for the filters package are found here: https://github.com/axlrommel/filters-image
    
    - After the background is removed the code reads the red, green and blue histograms of the image. They get put in three 255 int arrays. 
    
    - We then proceed to read all 36000 histograms ( 3 histograms x 12000 images) from the dataset which are in the Redis database. We run a correlation on the 3 histograms from the image versus the 36000 histograms from the dataset, and we select the images with the highest correlation. 
    
    - We then proceed to return those images to the client so it can display them. 
    
## Building and running:

`mvn install:install-file -Dfile=./lib/Filters.jar -DgroupId=ImageFilters -DartifactId=Filters -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true`

`mvn compile`

`mvn install`

`docker build -t what-is-that-bird .`

`docker run -it -p 8080:8080 what-is-that-bird`

[go to http://localhost:8080/](http://localhost:8080/)

questions? <rommelvillagomez@hotmail.com>


## docker clean stuff

`docker image prune -f`

`docker container prune -f`

`docker rmi $(docker images -q)`

`docker rm -v $(docker ps -qa)`

## lightsail deploy

`aws lightsail push-container-image --service-name what-is-that-bird --label what-is-that-bird --image what-is-that-bird`
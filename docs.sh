cd /tmp
git clone git@github.com:GetStream/stream-java.git
cd stream-java/
git checkout refs/tags/stream-java-1.3.3
mvn javadoc:aggregate
git checkout gh-pages
cp -fR target/site/apidocs/* .
git add --all
git commit -m "Updated javadocs"
git push origin gh-pages

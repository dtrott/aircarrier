varying vec4 position;

void main()
{
	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * gl_Vertex;
	position = gl_Vertex;
}
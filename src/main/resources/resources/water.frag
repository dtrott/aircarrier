varying vec4 position;

void main()
{
	vec4 position2 = position;
	gl_FragColor = vec4(position2.x, position2.y, position2.z, 1);//refractionColor + reflectionColor;// + specular; 
}
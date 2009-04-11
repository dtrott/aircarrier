float3 expand (float3 v)
{
  return (v - 0.5) * 2;
}

void main (float reflectionFactor : COLOR,
           float3 R               : TEXCOORD0,
           float3 TRed            : TEXCOORD1,
           float3 TGreen          : TEXCOORD2,
           float3 TBlue           : TEXCOORD3,
           float2 inTexcoord      : TEXCOORD4,
           float3 lightDirection  : TEXCOORD5,

       out float4 color : COLOR,

   uniform samplerCUBE cubeMap0,
   uniform samplerCUBE cubeMap1,
   uniform samplerCUBE cubeMap2,
   uniform samplerCUBE cubeMap3,
   uniform sampler2D normalMap)
{
  float4 reflectedColor = texCUBE (cubeMap0, R);

  float4 refractedColor;
  refractedColor.r = texCUBE (cubeMap1, TRed).r;
  refractedColor.g = texCUBE (cubeMap2, TRed).g;
  refractedColor.b = texCUBE (cubeMap3, TRed).b;
  refractedColor.a = 1;

  float3 light = normalize (lightDirection);
  float3 normal = expand (tex2D (normalMap, inTexcoord).xyz);

  float diffuse = max (dot (normal, light), 0);

  color = lerp (refractedColor, reflectedColor, reflectionFactor);
  color = saturate (0.25 + diffuse) * color;
  color.a = 0.6;
}

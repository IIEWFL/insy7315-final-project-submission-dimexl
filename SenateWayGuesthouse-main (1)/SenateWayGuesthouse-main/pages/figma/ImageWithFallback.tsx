import React, { useState } from 'react';
import { cn } from '../../lib/utils';

// ===== Props interface for ImageWithFallback component =====
interface ImageWithFallbackProps extends React.ImgHTMLAttributes<HTMLImageElement> {
  src: string;
  alt: string;
  fallbackSrc?: string;
}

export function ImageWithFallback({ 
  src, 
  alt, 
  fallbackSrc = '/images/Senate1.jpg', 
  className, 
  ...props 
}: ImageWithFallbackProps) {
  //  // ===== State for the current image source =====
  const [imgSrc, setImgSrc] = useState(src);
  const [hasError, setHasError] = useState(false);

  const handleError = () => {
    if (!hasError) {
      setHasError(true);
      setImgSrc(fallbackSrc);
    }
  };

  return (
    <img
      src={imgSrc}
      alt={alt}
      className={cn(className)}
      onError={handleError}
      {...props}
    />
  );
}
//https://www.figma.com/

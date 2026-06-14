{ pkgs, ... }:
{
  languages = {
    java = {
      enable = true;
      lsp.enable = true;
      maven.enable = true;
      jdk.package = pkgs.jdk21;
      gradle = {
        enable = true;
        package = pkgs.gradle_9;
      };
    };
  };
  packages = with pkgs; [
    prettier
    eslint
  ];
  env = {
  };
}
